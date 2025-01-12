package ru.kozodoy.IS1.Management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;

import ru.kozodoy.IS1.Repositories.ImportHistoryRepository;

@Service
@RequestScope
public class ImportHistoryService {

    @Autowired
    ImportHistoryRepository importHistoryRepository;

    public ImportHistory createNewInstance(Userz userz) {
        return new ImportHistory(userz);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveInstance(ImportHistory importHistory) {
        importHistoryRepository.save(importHistory);
    }
}
