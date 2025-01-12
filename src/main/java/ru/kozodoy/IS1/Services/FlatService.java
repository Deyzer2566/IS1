package ru.kozodoy.IS1.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;
import ru.kozodoy.IS1.Entities.Coordinates;
import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Entities.House;
import ru.kozodoy.IS1.Management.BadTokenException;
import ru.kozodoy.IS1.Management.ChangeType;
import ru.kozodoy.IS1.Management.History;
import ru.kozodoy.IS1.Management.ExportHistory;
import ru.kozodoy.IS1.Management.ExportHistoryService;
import ru.kozodoy.IS1.Management.ExportStatus;
import ru.kozodoy.IS1.Management.UserService;
import ru.kozodoy.IS1.Management.UsersFlats;
import ru.kozodoy.IS1.Management.Userz;
import ru.kozodoy.IS1.Management.WrongFlatOwnerException;
import ru.kozodoy.IS1.Repositories.CoordinatesRepository;
import ru.kozodoy.IS1.Repositories.FlatRepository;
import ru.kozodoy.IS1.Repositories.HistoryRepository;
import ru.kozodoy.IS1.Repositories.HouseRepository;
import ru.kozodoy.IS1.Repositories.UsersFlatsRepository;

class FlatWithAccessRights {
    private Flat flat;
    private Boolean isOwner;
    public FlatWithAccessRights(Flat flat, Boolean isOwner) {
        this.flat = flat;
        this.isOwner = isOwner;
    }
    public Boolean getIsOwner(){
        return isOwner;
    }

    public Flat getFlat(){
        return flat;
    }
}

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

    @Autowired
    ExportHistoryService exportHistoryService;

    public Optional<Flat> findById(Long id){
        return flatRepository.findById(id);
    }

    public List<Flat> findAll(){
        return flatRepository.findAll();
    }

    private Flat connectFlatToOtherEntities(Flat flat) {
        if(flat.getHouse() == null)
            return flat;
        Optional<House> house = houseRepository.findByNameAndYearAndNumberOfFloorsAndNumberOfFlatsOnFloorAndNumberOfLifts(
            flat.getHouse().getName(),
            flat.getHouse().getYear(),
            flat.getHouse().getNumberOfFloors(),
            flat.getHouse().getNumberOfFlatsOnFloor(),
            flat.getHouse().getNumberOfLifts());
        if (house.isPresent())
            flat.setHouse(house.get());
        return flat;
    }

    @Transactional
    public Flat addFlat(Userz user, Flat flat) {
        flat.setCreationDate(LocalDateTime.now());
        Flat flat1 = flatRepository.save(connectFlatToOtherEntities(flat));
        Optional<UsersFlats> usersFlats = usersFlatsRepository.findByUserAndFlat(user, flat1);
        if(!usersFlats.isPresent())
            usersFlatsRepository.save(new UsersFlats(user, flat1));
        historyRepository.save(new History(user, flat1.getId(), ChangeType.CREATE));
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
        House house = flatRepository.findById(id).get().getHouse();
        flat.setId(id);
        if(!usersFlatsRepository.findByUserAndFlat(user, flatRepository.findById(id).get()).isPresent() && !user.getIsAdmin())
            throw new WrongFlatOwnerException();
        connectFlatToOtherEntities(flat);
        flat = flatRepository.save(connectFlatToOtherEntities(flat));
        if(house != null && flatRepository.findByHouse(house).size() == 0)
            houseRepository.delete(house);
        historyRepository.save(new History(user, flat.getId(), ChangeType.UPDATE));
        return flat;
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
    public void deleteFlat(Userz user, Long id) throws WrongFlatOwnerException {
        House house = flatRepository.findById(id).get().getHouse();
        Flat flat = flatRepository.findById(id).get();
        if(!usersFlatsRepository.findByUserAndFlat(user, flat).isPresent() && !user.getIsAdmin())
            throw new WrongFlatOwnerException();
        usersFlatsRepository.delete(usersFlatsRepository.findByFlat(flat).get());
        if(flat.getHouse() != null && flatRepository.findByHouse(house).size() == 0)
            houseRepository.delete(house);
        historyRepository.save(new History(user, flat.getId(), ChangeType.DELETE));
    }

    public void deleteFlatByToken(String token, Long id) throws WrongFlatOwnerException, BadTokenException {
        Userz user;
        try{
            user = userService.getUserByToken(token);
        } catch (NoSuchElementException e){
            throw new BadTokenException();
        }
        deleteFlat(user, id);
    }

    public List<House> getHouses() {
        return houseRepository.findAll();
    }

    public List<Long> getFlatsWithRights(String token) throws BadTokenException {
        Userz user;
        try{
            user = userService.getUserByToken(token);
        } catch (NoSuchElementException e){
            throw new BadTokenException();
        }
        List<UsersFlats> usersFlats = usersFlatsRepository.findAll();
        return usersFlats.stream().filter(x->(user.getIsAdmin() || x.getUser().equals(user))).map(x->x.getFlat().getId()).toList();
    }

    public Userz getFlatOwner(Long flatId) throws NoSuchElementException {
        return usersFlatsRepository.findByFlat(flatRepository.findById(flatId).get()).get().getUser();
    }

    @Transactional
    public void importManyObjects(List<Flat> flats, String token) throws BadTokenException, ConstraintViolationException {
        Userz user;
        try {
            user = userService.getUserByToken(token);
        } catch (NoSuchElementException e) {
            throw new BadTokenException();
        }
        ExportHistory exportHistory = exportHistoryService.createNewInstance(user);
        try {
            flats.stream().forEach(x -> addFlat(user, x));
            exportHistory.setExportStatus(ExportStatus.SUCCESS);
            exportHistory.setFlatsAdded(Long.valueOf(flats.size()));
        } catch (ConstraintViolationException e) {
            exportHistory.setExportStatus(ExportStatus.FAIL);
            throw e;
        } finally {
            exportHistoryService.saveInstance(exportHistory);
        }
    }

}
