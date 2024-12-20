package ru.kozodoy.IS1.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.kozodoy.IS1.Entities.Coordinates;
import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Entities.House;
import ru.kozodoy.IS1.Entities.View;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {
    List<Flat> findByCoordinates(Coordinates coordinates);
    List<Flat> findByHouse(House house);

    @Query("SELECT SUM(f.numberOfRooms) from Flat f")
    Long sumNumberOfRooms();

    @Query(value = "SELECT COUNT(*) FROM flat WHERE view < ?1", nativeQuery = true)
    Long countWithViewLower(View view);

    @Query(value = "SELECT * FROM flat WHERE time_to_metro_by_transport < ?1", nativeQuery = true)
    List<Flat> findAllWithTimeToMetroByTransportLowerThan(Double timeToMetroByTransport);

    @Query(value = "SELECT * FROM flat WHERE balcony ORDER BY price LIMIT 1", nativeQuery = true)
    Flat getCheapestHataWithBalcon();
}