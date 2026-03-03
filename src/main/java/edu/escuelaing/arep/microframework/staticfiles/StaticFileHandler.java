package edu.escuelaing.arep.microframework.staticfiles;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StaticFileHandler {
    private final String root;

    public StaticFileHandler(String root) {
        this.root = normalizeRoot(root);
    }

    public byte[] load(String requestPath) throws IOException {
        String normalizedPath = normalizeRequestPath(requestPath);
        try (InputStream stream = getClass().getResourceAsStream(root + normalizedPath)) {
            if (stream == null) {
                return null;
            }
            return stream.readAllBytes();
        }
    }

    public String contentType(String requestPath) {
        String lowerPath = requestPath.toLowerCase();
        if (lowerPath.endsWith(".html") || lowerPath.endsWith(".htm")) {
            return "text/html; charset=UTF-8";
        }
        if (lowerPath.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        }
        if (lowerPath.endsWith(".js")) {
            return "application/javascript; charset=UTF-8";
        }
        if (lowerPath.endsWith(".png")) {
            return "image/png";
        }
        if (lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lowerPath.endsWith(".gif")) {
            return "image/gif";
        }
        if (lowerPath.endsWith(".svg")) {
            return "image/svg+xml";
        }
        if (lowerPath.endsWith(".json")) {
            return "application/json; charset=UTF-8";
        }
        return "application/octet-stream";
    }

    private static String normalizeRoot(String root) {
        if (root == null || root.isBlank()) {
            return "/webroot";
        }
        return root.startsWith("/") ? root : "/" + root;
    }

    private static String normalizeRequestPath(String requestPath) {
        String candidate = (requestPath == null || requestPath.equals("/")) ? "/index.html" : requestPath;
        if (candidate.contains("..")) {
            return "/index.html";
        }
        return candidate;
    }

    public static byte[] notFoundBody(String path) {
        String body = "404 Not Found: " + path;
        return body.getBytes(StandardCharsets.UTF_8);
    }
}

