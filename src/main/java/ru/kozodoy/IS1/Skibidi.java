package ru.kozodoy.IS1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Skibidi {

    @Autowired
    FlatService flatService;

    @GetMapping("/skibidi")
    public String lol() {
        return "skibidi toilet";
    }

}