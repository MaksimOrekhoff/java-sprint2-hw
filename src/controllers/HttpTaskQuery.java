package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTaskQuery {
    private static final String link = "http://localhost:8080/tasks/";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public void postTask(String typeTask, String bodyQuery) throws IOException, InterruptedException {
        URI uri = URI.create(link + typeTask);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(bodyQuery))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }

    public void getAllTask(String typeTask) throws IOException, InterruptedException {
        System.out.println(typeTask);
        URI uri = URI.create(link + typeTask);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }
    public void getTaskOnId(String typeTask, int id) throws IOException, InterruptedException {
        System.out.println(id);
        URI uri = URI.create(link + typeTask + "/?id=" +  id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }

    public void deleteTaskOnId(String typeTask, int id) throws IOException, InterruptedException {
        System.out.println(id);
        URI uri = URI.create(link + typeTask + "/?id=" +  id);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }
}
