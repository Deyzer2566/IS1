package ru.kozodoy.IS1.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(1)
    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotNull
    @NotEmpty
    private String name; // Поле не может быть null, Строка не может быть пустой

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    private Coordinates coordinates; // Поле не может быть null

    @NotNull
    @Column(updatable = false)
    private LocalDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Positive
    private double area; // Значение поля должно быть больше 0

    @Positive
    private Integer price; // Значение поля должно быть больше 0

    @NotNull
    private Boolean balcony; // Поле не может быть null

    @Positive
    private int timeToMetroOnFoot; // Значение поля должно быть больше 0

    @Positive
    private int numberOfRooms; // Значение поля должно быть больше 0

    @Positive
    private double timeToMetroByTransport; // Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    private Furnish furnish; // Поле может быть null

    @Enumerated(EnumType.STRING)
    private View view; // Поле может быть null

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "house_id")
    private House house; // Поле может быть null

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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getBalcony() {
        return balcony;
    }

    public void setBalcony(Boolean balcony) {
        this.balcony = balcony;
    }

    public int getTimeToMetroOnFoot() {
        return timeToMetroOnFoot;
    }

    public void setTimeToMetroOnFoot(int timeToMetroOnFoot) {
        this.timeToMetroOnFoot = timeToMetroOnFoot;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public double getTimeToMetroByTransport() {
        return timeToMetroByTransport;
    }

    public void setTimeToMetroByTransport(double timeToMetroByTransport) {
        this.timeToMetroByTransport = timeToMetroByTransport;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public void setFurnish(Furnish furnish) {
        this.furnish = furnish;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
