package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String token;
    private final String url;
    private final static HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    private final static HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String url) throws IOException, InterruptedException {
        URI uri = URI.create(url + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        this.token = response.body();
        this.url = url;
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        return response.body();
    }
}
