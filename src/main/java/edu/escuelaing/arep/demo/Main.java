package edu.escuelaing.arep.demo;

import static edu.escuelaing.arep.microframework.MicroWeb.get;
import static edu.escuelaing.arep.microframework.MicroWeb.port;
import static edu.escuelaing.arep.microframework.MicroWeb.start;
import static edu.escuelaing.arep.microframework.MicroWeb.staticfiles;

public class Main {
    public static void main(String[] args) throws Exception {
        staticfiles("/webroot");

        get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/App/pi", (req, resp) -> String.valueOf(Math.PI));

        port(8080);
        start();
    }
}

