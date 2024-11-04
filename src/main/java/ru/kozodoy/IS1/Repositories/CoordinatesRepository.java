package ru.kozodoy.IS1.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import ru.kozodoy.IS1.Entities.Coordinates;
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {

    Optional<Coordinates> findByXAndY(int x, Long y);
}