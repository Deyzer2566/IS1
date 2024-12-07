package ru.kozodoy.IS1.Management;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    Userz userz;

    public Application(){

    }

    public Application(Userz userz){
        this.userz = userz;
    }

    public Long getId(){
        return id;
    }

    public Userz getUserz(){
        return userz;
    }
}
