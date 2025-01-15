package ru.kozodoy.IS1.AdditionalStuff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.kozodoy.IS1.Entities.Coordinates;
import ru.kozodoy.IS1.Repositories.CoordinatesRepository;

@Component
public class CustomValidator {

    @Autowired
    public CoordinatesRepository coordinatesRepository;

    public boolean isValid(Coordinates coordinates) {
        return !coordinatesRepository.existsByXAndY(coordinates.getX(), coordinates.getY());
    }
}