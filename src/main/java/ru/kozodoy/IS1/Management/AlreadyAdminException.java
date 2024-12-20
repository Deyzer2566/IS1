package ru.kozodoy.IS1.Management;

public class AlreadyAdminException extends Exception{
    public AlreadyAdminException() {
        super();
    }

    public AlreadyAdminException(String message) {
        super(message);
    }
}
