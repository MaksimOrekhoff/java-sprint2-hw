package controllers;

import java.io.IOException;

public class ManagerSaveException extends IOException {
    @Override
    public String getMessage() {
        return "Ошибка";
    }
}
