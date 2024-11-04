package ru.kozodoy.IS1.Management;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(){
        super();
    }

    public WrongPasswordException(String message){
        super(message);
    }
}
