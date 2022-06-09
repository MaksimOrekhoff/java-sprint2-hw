package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final RequestHandler requestHandler = new RequestHandler();
    public static final HttpTaskManager httpTaskManager;

    static {
        try {
            httpTaskManager = new HttpTaskManager(Managers.getDefaultHistory(), "http://localhost:8078/");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpTaskManager getHttpTaskManager() {
        return httpTaskManager;
    }

    public static void mai() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/", new TaskHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }



    public static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks/ запроса от клиента.");

            Response answer = null;
            try {
                answer = requestHandler.response(httpExchange, httpTaskManager);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            httpExchange.sendResponseHeaders(answer.getCode(), 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(answer.getResponse().getBytes());
            }
        }
    }




}
