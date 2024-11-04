package ru.kozodoy.IS1.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kozodoy.IS1.Management.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
}
