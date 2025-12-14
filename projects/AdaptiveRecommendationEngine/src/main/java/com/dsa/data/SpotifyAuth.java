package com.dsa.data;

import com.dsa.core.SpotifyConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class SpotifyAuth {
    private static String accessToken;
    private static String refreshToken;

    // Spin up a local server to catch the callback
    public static String authenticate() throws Exception {
        String scope = "user-read-private user-read-email user-top-read user-library-read streaming user-read-currently-playing user-read-playback-state user-modify-playback-state";
        String loginUrl = SpotifyConfig.AUTH_URL +
                "?response_type=code" +
                "&client_id=" + SpotifyConfig.CLIENT_ID +
                "&scope=" + java.net.URLEncoder.encode(scope, StandardCharsets.UTF_8) +
                "&redirect_uri=" + java.net.URLEncoder.encode(SpotifyConfig.REDIRECT_URI, StandardCharsets.UTF_8);

        // Open Browser
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(loginUrl));
        } else {
            System.out.println("Open this URL: " + loginUrl);
        }

        // Wait for callback
        CountDownLatch latch = new CountDownLatch(1);
        final String[] authCode = new String[1];

        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
        server.createContext("/callback", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.contains("code=")) {
                authCode[0] = query.split("code=")[1].split("&")[0];
                String response = "<html><body style='background:#121212;color:#1db954;font-family:sans-serif;text-align:center;margin-top:100px;'><h1>Login Successful!</h1><p>You can close this window and return to the app.</p></body></html>";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            latch.countDown();
        });
        server.start();

        System.out.println("Waiting for login...");
        latch.await();
        server.stop(0);

        return exchangeCodeForToken(authCode[0]);
    }

    private static String exchangeCodeForToken(String code) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(
                (SpotifyConfig.CLIENT_ID + ":" + SpotifyConfig.CLIENT_SECRET).getBytes());

        String body = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + SpotifyConfig.REDIRECT_URI;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SpotifyConfig.TOKEN_URL))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        accessToken = json.get("access_token").getAsString();
        if (json.has("refresh_token")) {
            refreshToken = json.get("refresh_token").getAsString();
        }

        return accessToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }
}
