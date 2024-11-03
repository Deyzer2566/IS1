package ru.kozodoy.IS1.Entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;

@Entity
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Первичный ключ, генерируется автоматически

    private String name; // Поле может быть null

    @Positive
    private int year; // Значение поля должно быть больше 0

    @Positive
    private Integer numberOfFloors; // Значение поля должно быть больше 0

    @Positive
    private long numberOfFlatsOnFloor; // Значение поля должно быть больше 0

    @Positive
    private int numberOfLifts; // Значение поля должно быть больше 0

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Integer getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(Integer numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public long getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    public void setNumberOfFlatsOnFloor(long numberOfFlatsOnFloor) {
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    public int getNumberOfLifts() {
        return numberOfLifts;
    }

    public void setNumberOfLifts(int numberOfLifts) {
        this.numberOfLifts = numberOfLifts;
    }
}