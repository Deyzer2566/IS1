package ru.kozodoy.IS1.Management;

public class AlreadyExistException extends Exception {
    public AlreadyExistException() {
        super("Заявка уже существует");
    }

    public AlreadyExistException(String message) {
        super(message);
    }
}
