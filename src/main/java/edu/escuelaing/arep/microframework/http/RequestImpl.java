package edu.escuelaing.arep.microframework.http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestImpl implements Request {
    private final String method;
    private final String path;
    private final Map<String, String> queryParams;

    public RequestImpl(String method, String path, String rawQuery) {
        this.method = method;
        this.path = path;
        this.queryParams = parseQuery(rawQuery);
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getValues(String key) {
        return queryParams.get(key);
    }

    @Override
    public Map<String, String> getQueryParams() {
        return Collections.unmodifiableMap(queryParams);
    }

    public static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> parsed = new LinkedHashMap<>();
        if (rawQuery == null || rawQuery.isBlank()) {
            return parsed;
        }
        String[] pairs = rawQuery.split("&");
        for (String pair : pairs) {
            if (pair.isBlank()) {
                continue;
            }
            String[] tokens = pair.split("=", 2);
            String key = decode(tokens[0]);
            String value = tokens.length > 1 ? decode(tokens[1]) : "";
            parsed.put(key, value);
        }
        return parsed;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}

