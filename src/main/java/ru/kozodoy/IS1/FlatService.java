package ru.kozodoy.IS1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlatService {

    @Autowired
    private FlatRepository personRepository;
}