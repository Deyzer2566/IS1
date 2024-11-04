package ru.kozodoy.IS1.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Management.UsersFlats;
import ru.kozodoy.IS1.Management.Userz;

public interface UsersFlatsRepository extends JpaRepository<UsersFlats, Long>{
    List<UsersFlats> findByUser(Userz userz);
    List<UsersFlats> findByFlat(Flat flat);
    Optional<UsersFlats> findByUserAndFlat(Userz user, Flat flat);
}
