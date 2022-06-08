import controllers.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer.mai();
//        HttpTaskQuery httpTaskQuery = new HttpTaskQuery();
//
//        String bodyTask = "{\n" +
//                "\t\t\"typeTask\": \"TASK\",\n" +
//                "\t\t\"name\": \"первая\",\n" +
//                "\t\t\"description\": \"ывпа\",\n" +
//                "\t\t\"identificationNumber\": 2,\n" +
//                "\t\t\"status\": \"DONE\",\n" +
//                "\t\t\"duration\": 50,\n" +
//                "\t\t\"startTime\": \"null\"\n" +
//                "\t}";
//        httpTaskQuery.postTask("task", bodyTask);
//        System.out.println(kvServer.data);
//
//        httpTaskQuery.getTaskOnId("task", 1);
//
//        HttpTaskManager httpTaskManager = new HttpTaskManager(Managers.getDefaultHistory(), "http://localhost:8078/");
//
//        System.out.println(httpTaskManager.getTasks());
//
//        for (String key : httpTaskManager.key) {
//            httpTaskManager.load(key);
//        }
//
//
//
//        System.out.println(httpTaskManager.getHistoryManagers());
//        System.out.println(kvServer.data);
//
//        System.out.println(httpTaskManager.getTasks().equals(HttpTaskServer.httpTaskManager.getTasks()));
//        System.out.println(httpTaskManager.equals(HttpTaskServer.httpTaskManager));

   //     System.out.println(httpTaskManager.getHistoryManagers().equals(HttpTaskServer.httpTaskManager.getHistoryManagers()));
       // System.out.println(httpTaskManager.equals(HttpTaskServer.httpTaskManager));
//
//        String bodyEpic = "{\n" +
//                "\t\t\"subtasksEpic\": {\n" +
//                "\t\t\t\n" +
//                "\t\t},\n" +
//                "\t\t\"typeTask\": \"EPIC\",\n" +
//                "\t\t\"name\": \"третий\",\n" +
//                "\t\t\"description\": \"ывапр\",\n" +
//                "\t\t\"identificationNumber\": 2,\n" +
//                "\t\t\"status\": \"NEW\",\n" +
//                "\t\t\"duration\": 50,\n" +
//                "\t\t\"startTime\": {\n" +
//                "\t\t\t\"date\": {\n" +
//                "\t\t\t\t\"year\": 2022,\n" +
//                "\t\t\t\t\"month\": 9,\n" +
//                "\t\t\t\t\"day\": 5\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"time\": {\n" +
//                "\t\t\t\t\"hour\": 12,\n" +
//                "\t\t\t\t\"minute\": 5,\n" +
//                "\t\t\t\t\"second\": 0,\n" +
//                "\t\t\t\t\"nano\": 0\n" +
//                "\t\t\t}\n" +
//                "\t\t}\n" +
//                "\t}";
//        httpTaskQuery.postTask("epic", bodyEpic);
//        httpTaskQuery.getTaskOnId("epic", 2);
//
//        String bodySubtask = "{\n" +
//                "\t\t\"connectionWithEpic\": 2,\n" +
//                "\t\t\"typeTask\": \"SUBTASK\",\n" +
//                "\t\t\"name\": \"четвертая\",\n" +
//                "\t\t\"description\": \"ывп\",\n" +
//                "\t\t\"identificationNumber\": 3,\n" +
//                "\t\t\"status\": \"NEW\",\n" +
//                "\t\t\"duration\": 50,\n" +
//                "\t\t\"startTime\": {\n" +
//                "\t\t\t\"date\": {\n" +
//                "\t\t\t\t\"year\": 2022,\n" +
//                "\t\t\t\t\"month\": 9,\n" +
//                "\t\t\t\t\"day\": 5\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"time\": {\n" +
//                "\t\t\t\t\"hour\": 12,\n" +
//                "\t\t\t\t\"minute\": 5,\n" +
//                "\t\t\t\t\"second\": 0,\n" +
//                "\t\t\t\t\"nano\": 0\n" +
//                "\t\t\t}\n" +
//                "\t\t}\n" +
//                "\t}";
//        httpTaskQuery.postTask("subtask", bodySubtask);
//        httpTaskQuery.getTaskOnId("subtask", 3);

    }


}
