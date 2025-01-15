package ru.kozodoy.IS1.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.validation.ConstraintViolationException;
import ru.kozodoy.IS1.Entities.*;
import ru.kozodoy.IS1.Management.BadTokenException;
import ru.kozodoy.IS1.Management.WrongFlatOwnerException;
import ru.kozodoy.IS1.Services.FlatService;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class FlatAndMessage {
    Flat flat;
    String message;

    public FlatAndMessage(Flat flat, String message) {
        this.flat = flat;
        this.message = message;
    }

    public Flat getFlat() {
        return flat;
    }

    public String getMessage() {
        return message;
    }
}

class MessageDeletedAndMessage {
    String message;

    public MessageDeletedAndMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

@RestController
@RequestMapping("/api/flat")
public class FlatController {

    @Autowired
    FlatService flatService;

    @GetMapping("/{id}")
    public ResponseEntity<Flat> getFlat(@PathVariable Long id) {
        return flatService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Flat> getAllFlat(@RequestParam Optional<String> sortBy, @RequestParam Optional<Boolean> desc, @RequestParam Optional<String> filterBy, @RequestParam Optional<String> filterValue) {
        List<Flat> flats = flatService.findAll();
        if(sortBy.isPresent())
            flats = flats.stream().sorted((x1,x2)->{
                if(sortBy.get().equals("name")) {
                    if(desc.isPresent() && desc.get()) {
                        return x1.getName().compareTo(x2.getName());
                    }
                    else {
                        return x2.getName().compareTo(x1.getName());
                    }
                } else if(sortBy.get().equals("furnish")) {
                    if(desc.isPresent() && desc.get()) {
                        return x1.getFurnish().compareTo(x2.getFurnish());
                    }
                    else {
                        return x2.getFurnish().compareTo(x1.getFurnish());
                    }
                } else if(sortBy.get().equals("view")) {
                    if(desc.isPresent() && desc.get()) {
                        return x1.getView().compareTo(x2.getView());
                    }
                    else {
                        return x2.getView().compareTo(x1.getView());
                    }
                }
                return 0;
            }).toList();
        if(filterBy.isPresent() && filterValue.isPresent())
            flats = flats.stream().filter((x)->{
                if(filterBy.get().equals("name")) {
                    return x.getName().contains(filterValue.get());
                } else if(filterBy.get().equals("furnish")) {
                    return x.getFurnish().toString().contains(filterValue.get());
                } else if(filterBy.get().equals("view")) {
                    return x.getView().toString().contains(filterValue.get());
                }
                return false;
            }).toList();
        return flats;
    }

    @PostMapping
    public ResponseEntity<FlatAndMessage> addFlat(@RequestBody Flat flat, @RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok().body(new FlatAndMessage(
                flatService.addFlatByToken(token.replace("Bearer ", ""), flat),
                "Ok"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new FlatAndMessage(null, "Bad token"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlatAndMessage> updateFlat(@PathVariable Long id, @RequestBody Flat flat, @RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok().body(new FlatAndMessage(
                flatService.updateFlatByToken(token.replace("Bearer ", ""), id, flat),
                "Ok"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new FlatAndMessage(null, "Bad token"));
        } catch (WrongFlatOwnerException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new FlatAndMessage(null, "Wrong flat bro... Arent u trying fanum tax that bro??"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDeletedAndMessage> deleteFlat(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            flatService.deleteFlatByToken(token.replace("Bearer ", ""), id);
            return ResponseEntity.ok().body(new MessageDeletedAndMessage("Ok"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new MessageDeletedAndMessage( "Bad token"));
        } catch (WrongFlatOwnerException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new MessageDeletedAndMessage( "Wrong flat bro... Arent u trying fanum tax that bro??"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new MessageDeletedAndMessage( "Bruh..."));
        }
    }

    @GetMapping("/houses")
    public ResponseEntity<List<House>> getHouses(){
        return ResponseEntity.ok().body(flatService.getHouses());
    }

    @GetMapping("/canChange")
    public ResponseEntity<List<Long>> getFlatsWithRights(@RequestHeader("Authorization") String token){
        try {
            return ResponseEntity.ok().body(flatService.getFlatsWithRights(token.replace("Bearer ", "")));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
    }

    @GetMapping("/{flatId}/owner")
    public ResponseEntity<String> getOwner(@PathVariable Long flatId) {
        try {
            return ResponseEntity.ok().body(flatService.getFlatOwner(flatId).getLogin());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/export")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл не должен быть пустым");
        }

        try {
            // Создаем ObjectMapper с поддержкой YAML
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            // Читаем содержимое файла в список объектов
            List<Flat> objects = objectMapper.readValue(file.getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Flat.class));

            // Добавление объектов в БД
            flatService.importManyObjects(objects, token.replace("Bearer ", ""));
            return ResponseEntity.ok().build();
        } catch (IOException | ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Ошибка обработки файла: "+e.getMessage());
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
