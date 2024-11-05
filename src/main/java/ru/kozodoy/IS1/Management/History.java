package ru.kozodoy.IS1.Management;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Entities.Furnish;

@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    Userz userz;

    @ManyToOne()
    @JoinColumn(name = "flat_id", referencedColumnName = "id")
    @NotNull
    Flat flat;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ChangeType changeType;

    @NotNull
    LocalDateTime time;

    public History(Userz user, Flat flat, ChangeType changeType){
        this.userz = user;
        this.flat = flat;
        this.changeType = changeType;
        this.time = LocalDateTime.now();
    }

    public History(){
        
    }
}
