package ru.kozodoy.IS1.Management;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class ImportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    Userz userz;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ImportStatus importStatus;

    Long flatsAdded;    

    public ImportHistory(Userz user){
        this.userz = user;
    }

    public ImportHistory(){
        
    }

    public void setFlatsAdded(Long flatsAdded) {
        this.flatsAdded = flatsAdded;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }
}
