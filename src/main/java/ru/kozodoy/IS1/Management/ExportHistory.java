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
public class ExportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @NotNull
    Userz userz;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ExportStatus exportStatus;

    Long flatsAdded;

    public ExportHistory(Userz user){
        this.userz = user;
    }

    public ExportHistory(){
        
    }

    public void setFlatsAdded(Long flatsAdded) {
        this.flatsAdded = flatsAdded;
    }

    public void setExportStatus(ExportStatus exportStatus) {
        this.exportStatus = exportStatus;
    }

    public Userz getUserz() {
        return userz;
    }

    public Long getId() {
        return id;
    }

    public ExportStatus getExportStatus() {
        return exportStatus;
    }

    public Long getFlatsAdded() {
        return flatsAdded;
    }
}
