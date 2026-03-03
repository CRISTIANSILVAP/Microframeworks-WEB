package edu.escuelaing.arep.microframework.http;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestImplTest {

    @Test
    void shouldParseAndDecodeQueryParams() {
        Map<String, String> result = RequestImpl.parseQuery("name=Pedro%20Perez&city=Bogota");

        assertEquals("Pedro Perez", result.get("name"));
        assertEquals("Bogota", result.get("city"));
    }

    @Test
    void shouldHandleEmptyValue() {
        Map<String, String> result = RequestImpl.parseQuery("name=");

        assertEquals("", result.get("name"));
    }
}

