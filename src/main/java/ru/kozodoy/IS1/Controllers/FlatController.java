package ru.kozodoy.IS1.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.kozodoy.IS1.Entities.*;
import ru.kozodoy.IS1.Management.BadTokenException;
import ru.kozodoy.IS1.Management.WrongFlatOwnerException;
import ru.kozodoy.IS1.Services.FlatService;

import java.util.List;
import java.util.NoSuchElementException;

class FlatAndToken{
    Flat flat;
    String token;
    public String getToken(){
        return token;
    }
    public Flat getFlat(){
        return flat;
    }
}

class FlatAndMessage{
    Flat flat;
    String message;
    public FlatAndMessage(Flat flat, String message){
        this.flat = flat;
        this.message = message;
    }
    public Flat getFlat(){
        return flat;
    }
    public String getMessage(){
        return message;
    }
}

class Token{
    String token;
    public String getToken(){
        return token;
    }
}

class MessageDeletedAndMessage{
    boolean deleted;
    String message;
    public boolean getDeleted(){
        return deleted;
    }
    public String getMessage(){
        return message;
    }
    public MessageDeletedAndMessage(boolean deleted, String message){
        this.deleted = deleted;
        this.message = message;
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
    public List<Flat> getAllFlats(){
        return flatService.findAll();
    }

    @PostMapping
    public ResponseEntity<FlatAndMessage> addFlat(@RequestBody FlatAndToken flatAndToken) {
        try {
            return ResponseEntity.ok().body(new FlatAndMessage(
                flatService.addFlatByToken(flatAndToken.getToken(), flatAndToken.getFlat()),
                "Ok"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new FlatAndMessage(null,"Bad token"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlatAndMessage> updateFlat(@PathVariable Long id, @RequestBody FlatAndToken flatAndToken){
        try {
            return ResponseEntity.ok().body(new FlatAndMessage(
                flatService.updateFlatByToken(flatAndToken.getToken(), id, flatAndToken.getFlat()),
                "Ok"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(new FlatAndMessage(null,"Bad token"));
        } catch (WrongFlatOwnerException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new FlatAndMessage(null,"Wrong flat bro... Arent u trying fanum tax that bro??"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDeletedAndMessage> deleteFlat(@PathVariable Long id, @RequestBody Token token){
        try {
            return ResponseEntity.ok().body(new MessageDeletedAndMessage(
                flatService.deleteFlatByToken(token.getToken(), id),
                "Ok"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(new MessageDeletedAndMessage(false,"Bad token"));
        } catch (WrongFlatOwnerException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new MessageDeletedAndMessage(false,"Wrong flat bro... Arent u trying fanum tax that bro??"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new MessageDeletedAndMessage(false,"Bruh..."));
        }
    }

}