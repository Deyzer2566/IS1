package ru.kozodoy.IS1.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.kozodoy.IS1.Entities.Coordinates;
import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Entities.House;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {
    List<Flat> findByCoordinates(Coordinates coordinates);
    List<Flat> findByHouse(House house);
}