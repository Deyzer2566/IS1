package ru.kozodoy.IS1.Services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kozodoy.IS1.Entities.Coordinates;
import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Entities.House;
import ru.kozodoy.IS1.Management.BadTokenException;
import ru.kozodoy.IS1.Management.ChangeType;
import ru.kozodoy.IS1.Management.History;
import ru.kozodoy.IS1.Management.UserService;
import ru.kozodoy.IS1.Management.UsersFlats;
import ru.kozodoy.IS1.Management.Userz;
import ru.kozodoy.IS1.Management.WrongFlatOwnerException;
import ru.kozodoy.IS1.Repositories.CoordinatesRepository;
import ru.kozodoy.IS1.Repositories.FlatRepository;
import ru.kozodoy.IS1.Repositories.HistoryRepository;
import ru.kozodoy.IS1.Repositories.HouseRepository;
import ru.kozodoy.IS1.Repositories.UserRepository;
import ru.kozodoy.IS1.Repositories.UsersFlatsRepository;

@Service
public class FlatService {
    @Autowired
    FlatRepository flatRepository;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    UsersFlatsRepository usersFlatsRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    UserService userService;

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

    @Transactional
    public Flat addFlat(Userz user, Flat flat) {
        Flat flat1 = flatRepository.save(connectFlatToOtherEntities(flat));
        Optional<UsersFlats> usersFlats = usersFlatsRepository.findByUserAndFlat(user, flat1);
        if(!usersFlats.isPresent())
            usersFlatsRepository.save(new UsersFlats(user, flat1));
        historyRepository.save(new History(user, flat1, ChangeType.CREATE));
        return flat1;
    }

    public Flat addFlatByToken(String token, Flat flat) throws BadTokenException {
        Userz user;
        try{
            user = userService.getUserByToken(token);
        } catch (NoSuchElementException e){
            throw new BadTokenException();
        }
        return addFlat(user, flat);
    }

    @Transactional
    public Flat updateFlat(Userz user, Long id, Flat flat) throws WrongFlatOwnerException {
        if(!flatRepository.findById(id).isPresent()){
            return addFlat(user, flat);
        }
        Coordinates coordinates = flatRepository.findById(id).get().getCoordinates();
        House house = flatRepository.findById(id).get().getHouse();
        flat.setId(id);
        if(!usersFlatsRepository.findByUserAndFlat(user, flatRepository.findById(id).get()).isPresent() && !user.getIsAdmin())
            throw new WrongFlatOwnerException();
        Flat flat1 = flatRepository.save(connectFlatToOtherEntities(flat));
        if(flatRepository.findByCoordinates(coordinates).size() == 0)
            coordinatesRepository.delete(coordinates);
        if(flatRepository.findByHouse(house).size() == 0)
            houseRepository.delete(house);
        historyRepository.save(new History(user, flat1, ChangeType.UPDATE));
        return flat1;
    }

    public Flat updateFlatByToken(String token, Long id, Flat flat) throws WrongFlatOwnerException, BadTokenException{
        Userz user;
        try{
            user = userService.getUserByToken(token);
        } catch (NoSuchElementException e){
            throw new BadTokenException();
        }
        return updateFlat(user, id, flat);
    }

    @Transactional
    public boolean deleteFlat(Userz user, Long id) throws WrongFlatOwnerException {
        Coordinates coordinates = flatRepository.findById(id).get().getCoordinates();
        House house = flatRepository.findById(id).get().getHouse();
        Flat flat = flatRepository.findById(id).get();
        if(!usersFlatsRepository.findByUserAndFlat(user, flat).isPresent() && !user.getIsAdmin())
            throw new WrongFlatOwnerException();
        flatRepository.delete(flat);
        if(flatRepository.findByCoordinates(coordinates).size() == 0)
            coordinatesRepository.delete(coordinates);
        if(flatRepository.findByHouse(house).size() == 0)
            houseRepository.delete(house);
        historyRepository.save(new History(user, flat, ChangeType.DELETE));
        return true;
    }

    public boolean deleteFlatByToken(String token, Long id) throws WrongFlatOwnerException, BadTokenException {
        Userz user;
        try{
            user = userService.getUserByToken(token);
        } catch (NoSuchElementException e){
            throw new BadTokenException();
        }
        return deleteFlat(user, id);
    }

}
