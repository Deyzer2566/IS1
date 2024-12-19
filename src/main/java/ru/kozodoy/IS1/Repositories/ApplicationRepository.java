package ru.kozodoy.IS1.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kozodoy.IS1.Management.Application;
import ru.kozodoy.IS1.Management.Userz;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByUser(Userz userz);
}
