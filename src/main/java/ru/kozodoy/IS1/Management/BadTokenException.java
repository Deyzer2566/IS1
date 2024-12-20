package ru.kozodoy.IS1.Management;

public class BadTokenException extends Exception {
    public BadTokenException() {
        super("bad token");
    }
}
