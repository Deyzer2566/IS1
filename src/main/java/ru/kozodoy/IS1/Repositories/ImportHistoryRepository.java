package ru.kozodoy.IS1.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.kozodoy.IS1.Management.ImportHistory;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long> {
    
}
