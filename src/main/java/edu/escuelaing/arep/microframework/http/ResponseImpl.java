package edu.escuelaing.arep.microframework.http;

public class ResponseImpl implements Response {
    private int statusCode = 200;
    private String contentType = "text/plain; charset=UTF-8";

    @Override
    public void status(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public void type(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public int status() {
        return statusCode;
    }

    @Override
    public String type() {
        return contentType;
    }
}
