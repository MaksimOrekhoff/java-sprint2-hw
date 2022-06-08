package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTaskQuery {
    private static final String link = "http://localhost:8080/tasks/";
    // HTTP-клиент с настройками по умолчанию
    private static final HttpClient client = HttpClient.newHttpClient();

    // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
    private static final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public void postTask(String typeTask, String bodyQuery) throws IOException, InterruptedException {
        URI uri = URI.create(link + typeTask);

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .POST(HttpRequest.BodyPublishers.ofString(bodyQuery))    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "text/html") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
       // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);

        // выводим код состояния и тело ответа
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }

    public void getAllTask(String typeTask) throws IOException, InterruptedException {
        System.out.println(typeTask);
        URI uri = URI.create(link + typeTask);

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);

        // выводим код состояния и тело ответа
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }
    public void getTaskOnId(String typeTask, int id) throws IOException, InterruptedException {
        System.out.println(id);
        URI uri = URI.create(link + typeTask + "/?id=" +  id);

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);

        // выводим код состояния и тело ответа
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }

    public void deleteTaskOnId(String typeTask, int id) throws IOException, InterruptedException {
        System.out.println(id);
        URI uri = URI.create(link + typeTask + "/?id=" +  id);

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .DELETE()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);

        // выводим код состояния и тело ответа
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }


}
