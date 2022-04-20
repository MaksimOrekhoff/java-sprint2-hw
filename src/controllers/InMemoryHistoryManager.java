package controllers;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodePlace = new HashMap<>();
    private final CustomLinkedList taskCustomLinkedList = new CustomLinkedList();

    public void add(Task task) {
        int idTask = task.getIdentificationNumber();
        if (nodePlace.containsKey(idTask)) {
            remove(idTask);
        }
        taskCustomLinkedList.linkLast(task);
        nodePlace.put(idTask, taskCustomLinkedList.tail);
    }

    public List<Task> getHistory() {
        return taskCustomLinkedList.getTasks();
    }

    public void remove(int id) {
        if (nodePlace.containsKey(id)) {
            taskCustomLinkedList.removeNode(nodePlace.get(id));
            nodePlace.remove(id);
        }
    }

    public class CustomLinkedList {
        private Node head;
        private Node tail;

        public void linkLast(Task task) {
            final Node oldTail = tail;
            final Node newNode = new Node(oldTail, task, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
        }

        public void removeNode(Node node) {
            if (node.prev == null && node.next == null) {
                tail = null;
                head = null;
            } else if (node.prev == null) {
                head = node.next;
                node.next.prev = null;
            } else if (node.next == null) {
                node.prev.next = null;
                tail = node.prev;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        }

        public List<Task> getTasks() {
            Node node = head;
            List<Task> hist = new ArrayList<>();
            while (node != null) {
                hist.add(node.data);
                node = node.next;
            }
            return hist;
        }
    }
}


