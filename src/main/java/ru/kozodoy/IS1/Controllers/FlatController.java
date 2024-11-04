package ru.kozodoy.IS1.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.kozodoy.IS1.Entities.*;
import ru.kozodoy.IS1.Services.FlatService;

import java.util.List;
import java.util.Optional;

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
    public Flat addFlat(@RequestBody Flat flat) {
        return flatService.addFlat(flat);
    }

    @PutMapping("/{id}")
    public Flat updateFlat(@PathVariable Long id, @RequestBody Flat flat){
        return flatService.updateFlat(id, flat);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFlat(@PathVariable Long id){
        return flatService.deleteFlat(id);
    }

}