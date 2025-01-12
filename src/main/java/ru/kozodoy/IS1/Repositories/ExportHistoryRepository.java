package ru.kozodoy.IS1.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.kozodoy.IS1.Management.ExportHistory;
import ru.kozodoy.IS1.Management.Userz;

public interface ExportHistoryRepository extends JpaRepository<ExportHistory, Long> {
    List<ExportHistory> findByUserz(Userz userz);
}
