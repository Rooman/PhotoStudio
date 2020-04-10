package com.photostudio.web.ws;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

public class MyEndpointConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        Map<String, List<String>> headers = request.getHeaders();
        String cookies = headers.get("cookie").get(0);
        String token = extractUserTokenFromCookies(cookies);
        config.getUserProperties().put("user-token", token);
    }

    String extractUserTokenFromCookies(String cookies) {
        String[] cookiesArray = cookies.split(";");
        for (String cookie : cookiesArray) {
            if (cookie.contains("user-token")) {
                return cookie.substring(cookie.indexOf("=") + 1);
            }
        }
        return null;
    }
}