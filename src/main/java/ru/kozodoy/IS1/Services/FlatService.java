package ru.kozodoy.IS1.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kozodoy.IS1.Entities.Coordinates;
import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Entities.House;
import ru.kozodoy.IS1.Repositories.CoordinatesRepository;
import ru.kozodoy.IS1.Repositories.FlatRepository;
import ru.kozodoy.IS1.Repositories.HouseRepository;

@Service
public class FlatService {
    @Autowired
    FlatRepository flatRepository;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    CoordinatesRepository coordinatesRepository;

    public Optional<Flat> findById(Long id){
        return flatRepository.findById(id);
    }

    public List<Flat> findAll(){
        return flatRepository.findAll();
    }

    private Flat connectFlatToOtherEntities(Flat flat) {
        Optional<House> house = houseRepository.findByNameAndYearAndNumberOfFloorsAndNumberOfFlatsOnFloorAndNumberOfLifts(
            flat.getHouse().getName(),
            flat.getHouse().getYear(),
            flat.getHouse().getNumberOfFloors(),
            flat.getHouse().getNumberOfFlatsOnFloor(),
            flat.getHouse().getNumberOfLifts());
        if (house.isPresent())
            flat.setHouse(house.get());
        Optional<Coordinates> coordinates = coordinatesRepository.findByXAndY(
            flat.getCoordinates().getX(),
            flat.getCoordinates().getY());
        if (coordinates.isPresent())
            flat.setCoordinates(coordinates.get());
        return flat;
    }

    public Flat addFlat(Flat flat) {
        return flatRepository.save(connectFlatToOtherEntities(flat));
    }

    public Flat updateFlat(Long id, Flat flat) {
        Coordinates coordinates = flatRepository.findById(id).get().getCoordinates();
        House house = flatRepository.findById(id).get().getHouse();
        flat.setId(id);
        Flat flat1 = flatRepository.save(connectFlatToOtherEntities(flat));
        if(flatRepository.findByCoordinates(coordinates).size() == 0)
            coordinatesRepository.delete(coordinates);
        if(flatRepository.findByHouse(house).size() == 0)
            houseRepository.delete(house);
        return flat1;
    }

    @Transactional
    public boolean deleteFlat(Long id) {
        Coordinates coordinates = flatRepository.findById(id).get().getCoordinates();
        House house = flatRepository.findById(id).get().getHouse();
        flatRepository.delete(flatRepository.findById(id).get());
        if(flatRepository.findByCoordinates(coordinates).size() == 0)
            coordinatesRepository.delete(coordinates);
        if(flatRepository.findByHouse(house).size() == 0)
            houseRepository.delete(house);
        return true;
    }

}
