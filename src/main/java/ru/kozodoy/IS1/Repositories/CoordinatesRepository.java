package ru.kozodoy.IS1.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import ru.kozodoy.IS1.Entities.Coordinates;
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {

    List<Coordinates> findByXAndY(int x, Long y);

    @Query(value = "select exists(select 1 from coordinates where x = :x and y = :y)", nativeQuery = true)
    Boolean existsByXAndY(@Param("x") Integer x, @Param("y") Long y);
}