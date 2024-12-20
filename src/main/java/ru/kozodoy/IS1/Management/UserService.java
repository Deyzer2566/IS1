package ru.kozodoy.IS1.Management;

import java.time.LocalDateTime;
import java.beans.Transient;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.kozodoy.IS1.Repositories.ApplicationRepository;
import ru.kozodoy.IS1.Repositories.UserRepository;
import ru.kozodoy.IS1.Repositories.UsersFlatsRepository;

class TokenInfo {
    String token;
    LocalDateTime creationTime;

    public TokenInfo(String token) {
        this.token = token;
        creationTime = LocalDateTime.now();
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    UsersFlatsRepository usersFlatsRepository;

    MessageDigest passwordEncoder;

    HashMap<String, String> tokensInv;

    LinkedList<TokenInfo> tokenInfos;

    public UserService() {
        tokensInv = new HashMap<>();
        tokenInfos = new LinkedList<>();
        try {
            passwordEncoder = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // Handle exception
        }
    }

    public String getMD5(String str) {
        byte[] messageDigest = passwordEncoder.digest(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(messageDigest);
    }

    private TokenInfo generateToken(String login, String password) {
        String token = getMD5(login + password + LocalDateTime.now().toString());
        tokensInv.put(token, login);
        TokenInfo tokenInfo = new TokenInfo(token);
        tokenInfos.push(tokenInfo);
        return tokenInfo;
    }

    public TokenInfo getToken(String login, String password) throws WrongPasswordException {
        deleteTokens();
        password = getMD5(password);
        if (userRepository.findByLogin(login).get().getPassword().equals(password)) {
            return generateToken(login, password);
        } else {
            throw new WrongPasswordException("Smth goone 'rong, maybe u shouldn't bruteforce this poor guy??");
        }
    }

    @Transactional
    public TokenInfo register(String login, String password) throws LoginOccupiedException, PasswordOccupiedException {
        password = getMD5(password);
        if (userRepository.findByLogin(login).isPresent()) {
            throw new LoginOccupiedException("Логин занят!");
        }
        if (userRepository.findByPassword(password).isPresent()) {
            throw new PasswordOccupiedException("Пароль занят пользователем " + userRepository.findByPassword(password).get().getLogin());
        }
        Userz newUser = new Userz();
        newUser.setLogin(login);
        newUser.setPassword(password);
        newUser.setIsAdmin(userRepository.count() == 0);
        userRepository.save(newUser);
        return generateToken(login, password);
    }

    public void deleteTokens() {
        try {
            while (Duration.between(LocalDateTime.now(), tokenInfos.getFirst().getCreationTime()).toHours() >= 1) {
                tokensInv.remove(tokenInfos.removeFirst().getToken());
            }
        } catch (NoSuchElementException e) {
            // Handle exception
        }
    }

    public Userz getUserByToken(String token) throws NoSuchElementException {
        return userRepository.findByLogin(tokensInv.get(token)).get();
    }

    public Boolean isAdmin(String login) throws NoSuchElementException{
        Userz user;
        user = userRepository.findByLogin(login).get();
        return user.getIsAdmin();
    }

    @Transactional
    public void makeAdminApplication(String token) throws AlreadyAdminException, BadTokenException, AlreadyExistException {
        Userz user;
        try {
            user = getUserByToken(token);
        } catch (NoSuchElementException e) {
            throw new BadTokenException();
        }
        if (!user.getIsAdmin()) {
            if(applicationRepository.findByUserz(user).isEmpty())
                applicationRepository.save(new Application(user));
            else
                throw new AlreadyExistException("wait bruh, sigmas havent fanum taxed you yet");
        } else {
            throw new AlreadyAdminException("bruhh....");
        }
    }

    public List<Application> getApplications(String token) throws BadTokenException {
        Userz user;
        try {
            user = getUserByToken(token);
        } catch (NoSuchElementException e) {
            throw new BadTokenException();
        }
        if (!user.getIsAdmin()) {
            throw new BadTokenException();
        }
        return applicationRepository.findAll();
    }

    @Transactional
    public void makeAdmin(String token, Long id) throws BadTokenException, NoSuchElementException {
        Userz user;
        try {
            user = getUserByToken(token);
        } catch (NoSuchElementException e) {
            throw new BadTokenException();
        }
        if (!user.getIsAdmin()) {
            throw new BadTokenException();
        }
        Application application = applicationRepository.findById(id).get();
        application.userz.setIsAdmin(true);
        applicationRepository.save(application);
        applicationRepository.deleteById(id);
    }

    @Transactional
    public void rejectApplication(String token, Long id) throws BadTokenException, IllegalArgumentException {
        Userz user;
        try {
            user = getUserByToken(token);
        } catch (NoSuchElementException e) {
            throw new BadTokenException();
        }
        if (!user.getIsAdmin()) {
            throw new BadTokenException();
        }
        applicationRepository.deleteById(id);
    }
}
