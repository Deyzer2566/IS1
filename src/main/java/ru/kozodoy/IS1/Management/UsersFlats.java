package ru.kozodoy.IS1.Management;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ru.kozodoy.IS1.Entities.Flat;

@Entity
public class UsersFlats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    Userz user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="flat_id")
    Flat flat;

    public Userz getUser(){
        return user;
    }

    public Flat getFlat(){
        return flat;
    }

    public UsersFlats(Userz user, Flat flat){
        this.user = user;
        this.flat = flat;
    }

    public UsersFlats(){
        
    }
}
