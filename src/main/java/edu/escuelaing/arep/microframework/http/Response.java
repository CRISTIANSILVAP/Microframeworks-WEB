package edu.escuelaing.arep.microframework.http;

public interface Response {
    void status(int statusCode);

    void type(String contentType);

    int status();

    String type();
}

