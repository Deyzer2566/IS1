package ru.kozodoy.IS1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.kozodoy.IS1.Entities.Flat;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {
  Flat findById(long id);
}