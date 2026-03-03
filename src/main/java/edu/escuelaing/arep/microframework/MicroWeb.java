package edu.escuelaing.arep.microframework;

import edu.escuelaing.arep.microframework.routing.Route;
import edu.escuelaing.arep.microframework.server.HttpServerEngine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MicroWeb {
    private static final Map<String, Route> GET_ROUTES = new ConcurrentHashMap<>();
    private static volatile String staticFilesLocation = "/webroot";
    private static volatile int port = 8080;

    private MicroWeb() {
    }

    public static void get(String path, Route route) {
        GET_ROUTES.put(path, route);
    }

    public static void staticfiles(String path) {
        staticFilesLocation = path;
    }

    public static void port(int value) {
        port = value;
    }

    public static void start() throws IOException {
        HttpServerEngine server = new HttpServerEngine(port, GET_ROUTES, staticFilesLocation);
        server.start();
    }
}

