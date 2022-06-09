package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
 private String token;
    private final String url;
    private final static HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    private final static HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String url) throws IOException, InterruptedException {
        URI uri = URI.create(url + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        this.token = response.body();
        this.url = url;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        return response.body();
    }
}
