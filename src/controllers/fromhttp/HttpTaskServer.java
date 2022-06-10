package controllers.fromhttp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exep.ManagerSaveException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int PORT;
    private final RequestHandler requestHandler = new RequestHandler();
    private final HttpTaskManager httpTaskManager;
    private final String path;

    private HttpServer httpServer;

    public HttpTaskServer(int PORT, HttpTaskManager httpTaskManager, String path) {
        this.PORT = PORT;
        this.httpTaskManager = httpTaskManager;
        this.path = path;
    }

    public HttpTaskManager getHttpTaskManager() {
        return httpTaskManager;
    }

    public void createServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext(path, new TaskHandler());
    }

    public void startServer() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stopServer(int delay) {
        httpServer.stop(delay);
    }

    public class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                System.out.println("Началась обработка " + path + " запроса от клиента.");
                Response answer = requestHandler.response(httpExchange, httpTaskManager);
                httpExchange.sendResponseHeaders(answer.getCode(), 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(answer.getResponse().getBytes());
                }
            } catch (InterruptedException | IOException e) {
                throw new ManagerSaveException("Запрос не был выполнен.");
            }
        }
    }
}
