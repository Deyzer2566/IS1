package ru.kozodoy.IS1.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.kozodoy.IS1.Management.Userz;

public interface UserRepository extends JpaRepository<Userz, Long> {
    Optional<Userz> findByLogin(String login);
    Optional<Userz> findByPassword(String password);
}
