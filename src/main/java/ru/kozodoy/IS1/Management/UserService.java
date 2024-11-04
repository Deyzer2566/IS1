package ru.kozodoy.IS1.Management;

import java.time.LocalDateTime;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.kozodoy.IS1.Repositories.ApplicationRepository;
import ru.kozodoy.IS1.Repositories.UserRepository;

class TokenInfo {
    String token;
    LocalDateTime creationTime;
    public TokenInfo(String token){
        this.token = token;
        creationTime = LocalDateTime.now();
    }
    public String getToken(){
        return token;
    }
    public LocalDateTime getCreationTime(){
        return creationTime;
    }
}

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    MessageDigest passwordEncoder;

    HashMap<String, String> tokensInv;

    LinkedList<TokenInfo> tokenInfos;

    public UserService(){
        tokensInv = new HashMap<>();
        tokenInfos = new LinkedList<>();
        try{
            passwordEncoder = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e){

        }
    }

    public String getMD5(String str){
        byte [] messageDigest = passwordEncoder.digest(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(messageDigest);
    }

    private TokenInfo generateToken(String login, String password){
        String token = getMD5(login+password+LocalDateTime.now().toString());
        tokensInv.put(token,login);
        TokenInfo tokenInfo = new TokenInfo(token);
        tokenInfos.push(tokenInfo);
        return tokenInfo;
    }

    public TokenInfo getToken(String login, String password) throws WrongPasswordException {
        deleteTokens();
        password = getMD5(password);
        try {
            if (userRepository.findByLogin(login).get().getPassword().equals(password)){
                return generateToken(login, password);
            }
            else
                throw new WrongPasswordException();
        } catch(NoSuchElementException e) {
            throw new WrongPasswordException();
        }
    }

    public TokenInfo register(String login, String password) throws LoginOccupiedException, PasswordOccupiedException {
        password = getMD5(password);
        if(userRepository.findByLogin(login).isPresent())
            throw new LoginOccupiedException();
        if(userRepository.findByPassword(password).isPresent())
            throw new PasswordOccupiedException("Пароль занят пользователем "+userRepository.findByPassword(password).get().getLogin());
        Userz newUser = new Userz();
        newUser.setLogin(login);
        newUser.setPassword(password);
        newUser.setIsAdmin(userRepository.count()==0);
        userRepository.save(newUser);
        return generateToken(login, password);
    }

    public void deleteTokens(){
        try {
            while(Duration.between(LocalDateTime.now(), tokenInfos.getFirst().getCreationTime()).toHours()>=1){
                tokensInv.remove(tokenInfos.removeFirst().getToken());
            }
        } catch (NoSuchElementException e){
            
        }
    }

    private Userz getUserByToken(String token){
        System.out.println(tokensInv.get(token));
        return userRepository.findByLogin(tokensInv.get(token)).get();
    }

    public void makeAdmin(String token) throws AlreadyAdminException{
        Userz user = getUserByToken(token);
        if(!user.isAdmin())
            applicationRepository.save(new Application(user));
        else
            throw new AlreadyAdminException();
    }
}
