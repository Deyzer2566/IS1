package ru.kozodoy.IS1.Management;

public class PasswordOccupiedException extends Exception {
    public PasswordOccupiedException(){
        super();
    }

    public PasswordOccupiedException(String message){
        super(message);
    }
}
