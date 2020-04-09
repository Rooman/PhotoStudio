package com.photostudio.web.ws;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyEndpointConfiguratorTest {

    MyEndpointConfigurator myEndpointConfigurator = new MyEndpointConfigurator();

    @Test
    void testExtractUserTokenFromCookies() {
        String cookies = "JSESSIONID=9z6t6dez4dguil4434jxd9v5; user-token=e2701255-0276-44bb-9f82-6177a661d8ec";
        String actualToken = myEndpointConfigurator.extractUserTokenFromCookies(cookies);
        assertEquals("e2701255-0276-44bb-9f82-6177a661d8ec", actualToken);
    }
}