package ru.kozodoy.IS1.Management;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Userz { // postgres is a shit
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @NotEmpty
    String login;

    @NotNull
    @NotEmpty
    String password;

    @NotNull
    boolean _isAdmin;

    public void setLogin(String login){
        this.login = login;
    }

    public String getLogin(){
        return login;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public boolean isAdmin(){
        return _isAdmin;
    }

    public void setIsAdmin(boolean _isAdmin){
        this._isAdmin=_isAdmin;
    }
}
