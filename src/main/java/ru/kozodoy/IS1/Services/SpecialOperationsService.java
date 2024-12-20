package ru.kozodoy.IS1.Services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Entities.View;
import ru.kozodoy.IS1.Repositories.FlatRepository;

@Service
public class SpecialOperationsService {

    @Autowired
    private FlatRepository flatRepository;

    public Long sumNumberOfRooms() {
        return flatRepository.sumNumberOfRooms();
    }

    public Long countWithViewLower(View view) {
        return flatRepository.countWithViewLower(view.name());
    }

    public List<Flat> getAllWithTimeToMetroByTransportLowerThan(Double timeToMetroByTransport) {
        return flatRepository.findAllWithTimeToMetroByTransportLowerThan(timeToMetroByTransport);
    }

    public Flat getCheapestHataWithBalcon() {
        return flatRepository.getCheapestHataWithBalcon();
    }

    public Flat getTheMostExpensiveHata(Long flat1Id, Long flat2Id, Long flat3Id) throws NoSuchElementException {
        Flat flat1 = flatRepository.findById(flat1Id).get();
        Flat flat2 = flatRepository.findById(flat2Id).get();
        Flat flat3 = flatRepository.findById(flat3Id).get();
        if(flat1.getPrice() < flat2.getPrice()) {
            if(flat1.getPrice() < flat3.getPrice())
                return flat1;
            else
                return flat3;
        } else {
            if(flat2.getPrice() < flat3.getPrice())
                return flat2;
            else
                return flat3;
        }
    }
}
