package edu.escuelaing.arep.microframework.routing;

import edu.escuelaing.arep.microframework.http.Request;
import edu.escuelaing.arep.microframework.http.Response;

@FunctionalInterface
public interface Route {
    String handle(Request request, Response response) throws Exception;
}

