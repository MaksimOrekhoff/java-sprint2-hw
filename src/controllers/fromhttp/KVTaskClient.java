package controllers.fromhttp;

import exep.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String token;
    private String url;
    private final static HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    private final static HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String url) {
        URI uri = URI.create(url + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (InterruptedException | IOException e) {
            throw new ManagerSaveException("Ошибка регистрации.");
        }
        if (response.statusCode() != 200) {
            throw new ManagerSaveException("Клиент не был зарегистрирован.");
        }
        setToken(response.body());
        setUrl(url);
    }

    private void setToken(String token) {
        this.token = token;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Запрос не обработан.");
        }
        if (response.statusCode() != 200) {
            throw new ManagerSaveException("Задача не добавлена.");
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Запрос не обработан.");
        }
        if (response.statusCode() != 200) {
            System.out.println("Данные не получены.");
        }
        return response.body();
    }
}
