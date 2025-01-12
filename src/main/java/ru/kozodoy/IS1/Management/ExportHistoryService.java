package ru.kozodoy.IS1.Management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;

import ru.kozodoy.IS1.Repositories.ExportHistoryRepository;

@Service
@RequestScope
public class ExportHistoryService {

    @Autowired
    ExportHistoryRepository exportHistoryRepository;

    public ExportHistory createNewInstance(Userz userz) {
        return new ExportHistory(userz);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveInstance(ExportHistory exportHistory) {
        exportHistoryRepository.save(exportHistory);
    }
}
