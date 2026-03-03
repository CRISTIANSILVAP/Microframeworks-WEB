package edu.escuelaing.arep.microframework.http;

import java.util.Map;

public interface Request {
    String getMethod();

    String getPath();

    String getValues(String key);

    Map<String, String> getQueryParams();
}

