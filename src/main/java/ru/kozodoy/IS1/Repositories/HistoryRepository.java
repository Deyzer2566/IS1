package ru.kozodoy.IS1.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.kozodoy.IS1.Management.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
    
}
