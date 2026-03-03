package edu.escuelaing.arep.microframework.server;

import edu.escuelaing.arep.microframework.http.RequestImpl;
import edu.escuelaing.arep.microframework.http.ResponseImpl;
import edu.escuelaing.arep.microframework.routing.Route;
import edu.escuelaing.arep.microframework.staticfiles.StaticFileHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpServerEngine {
    private final int port;
    private final Map<String, Route> getRoutes;
    private final StaticFileHandler staticFileHandler;

    public HttpServerEngine(int port, Map<String, Route> getRoutes, String staticRoot) {
        this.port = port;
        this.getRoutes = getRoutes;
        this.staticFileHandler = new StaticFileHandler(staticRoot);
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor listo en http://localhost:" + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             OutputStream out = socket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isBlank()) {
                return;
            }

            String headerLine;
            while ((headerLine = in.readLine()) != null && !headerLine.isBlank()) {
                // Solo consumimos headers para liberar el stream.
            }

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 3) {
                writeResponse(out, 400, "text/plain; charset=UTF-8", "Bad Request".getBytes(StandardCharsets.UTF_8));
                return;
            }

            String method = tokens[0];
            URI uri = URI.create(tokens[1]);
            String path = uri.getPath();

            if (!"GET".equals(method)) {
                writeResponse(out, 405, "text/plain; charset=UTF-8", "Method Not Allowed".getBytes(StandardCharsets.UTF_8));
                return;
            }

            Route route = getRoutes.get(path);
            if (route != null) {
                handleRoute(out, route, method, path, uri.getQuery());
                return;
            }

            byte[] staticBody = staticFileHandler.load(path);
            if (staticBody != null) {
                writeResponse(out, 200, staticFileHandler.contentType(path), staticBody);
                return;
            }

            writeResponse(out, 404, "text/plain; charset=UTF-8", StaticFileHandler.notFoundBody(path));
        } catch (Exception e) {
            try {
                OutputStream out = clientSocket.getOutputStream();
                writeResponse(out, 500, "text/plain; charset=UTF-8", ("Internal Error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
            } catch (IOException ignored) {
                // No hay mucho por hacer si falla la escritura del error.
            }
        }
    }

    private void handleRoute(OutputStream out, Route route, String method, String path, String rawQuery) throws Exception {
        RequestImpl request = new RequestImpl(method, path, rawQuery);
        ResponseImpl response = new ResponseImpl();
        String body = route.handle(request, response);
        byte[] payload = body == null ? new byte[0] : body.getBytes(StandardCharsets.UTF_8);
        writeResponse(out, response.status(), response.type(), payload);
    }

    private void writeResponse(OutputStream out, int statusCode, String contentType, byte[] payload) throws IOException {
        String reason = reasonPhrase(statusCode);
        String headers = "HTTP/1.1 " + statusCode + " " + reason + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + payload.length + "\r\n"
                + "Connection: close\r\n\r\n";
        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(payload);
        out.flush();
    }

    private String reasonPhrase(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            default -> "Internal Server Error";
        };
    }
}

