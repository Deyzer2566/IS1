package ru.kozodoy.IS1.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import ru.kozodoy.IS1.Entities.House;
public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findByNameAndYearAndNumberOfFloorsAndNumberOfFlatsOnFloorAndNumberOfLifts(
            String name, int year, Integer numberOfFloors, long numberOfFlatsOnFloor, int numberOfLifts);
}
