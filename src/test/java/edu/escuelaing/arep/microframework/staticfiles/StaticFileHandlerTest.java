package edu.escuelaing.arep.microframework.staticfiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StaticFileHandlerTest {

    @Test
    void shouldLoadIndexHtmlFromConfiguredRoot() throws Exception {
        StaticFileHandler handler = new StaticFileHandler("/webroot");

        byte[] content = handler.load("/index.html");

        assertNotNull(content);
    }

    @Test
    void shouldResolveDefaultIndexForRootPath() throws Exception {
        StaticFileHandler handler = new StaticFileHandler("/webroot");

        byte[] rootContent = handler.load("/");
        byte[] indexContent = handler.load("/index.html");

        assertArrayEquals(indexContent, rootContent);
    }

    @Test
    void shouldDetectCssContentType() {
        StaticFileHandler handler = new StaticFileHandler("/webroot");

        assertEquals("text/css; charset=UTF-8", handler.contentType("/styles.css"));
    }
}

