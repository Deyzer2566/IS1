package ru.kozodoy.IS1.Management;

public class LoginOccupiedException extends Exception{
    public LoginOccupiedException(){
        super();
    }
    public LoginOccupiedException(String message){
        super(message);
    }
}
