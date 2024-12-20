package ru.kozodoy.IS1.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.kozodoy.IS1.Entities.Flat;
import ru.kozodoy.IS1.Entities.View;
import ru.kozodoy.IS1.Services.SpecialOperationsService;

@RestController
@RequestMapping("/api/smo")
public class SpecialOperationsController {
    @Autowired
    private SpecialOperationsService specialOperationsService;

    @GetMapping("/sumNumberOfRooms")
    public Long sumNumberOfRooms() {
        return specialOperationsService.sumNumberOfRooms();
    }

    @GetMapping("/countWithViewLower")
    public Long countWithViewLower(@RequestParam View view) {
        return specialOperationsService.countWithViewLower(view);
    }

    @GetMapping("/getAllWithTimeToMetroByTransportLowerThan")
    public List<Flat> getAllWithTimeToMetroByTransportLowerThan(@RequestParam Double timeToMetroByTransport) {
        return specialOperationsService.getAllWithTimeToMetroByTransportLowerThan(timeToMetroByTransport);
    }

    @GetMapping("/getCheapestHataWithBalcon")
    public Flat getCheapestHataWithBalcon() {
        return specialOperationsService.getCheapestHataWithBalcon();
    }

    @GetMapping("/theMostExpensiveFlat")
    public Flat getTheMostExpensiveHata(@RequestParam Long flat1, @RequestParam Long flat2, @RequestParam Long flat3) {
        return specialOperationsService.getTheMostExpensiveHata(flat1, flat2, flat3);
    }
}
