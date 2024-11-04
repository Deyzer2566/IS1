package ru.kozodoy.IS1.Management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class AuthorizationInfo{
    String login;
    String password;
    public String getLogin(){
        return login;
    }
    public String getPassword(){
        return password;
    }
}

class TokenRequestResponse{
    String token;
    String message;
    public TokenRequestResponse(String token, String message){
        this.token = token;
        this.message = message;
    }
    public String getToken(){
        return token;
    }
    public String getMessage(){
        return message;
    }
}

class Token{
    String token;
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return token;
    }
}

class SetAdminResponse{
    String message;
    public SetAdminResponse(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}

@RestController
@RequestMapping("/api/management")
public class ManagementController {

    @Autowired
    UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<TokenRequestResponse> getToken(@RequestBody AuthorizationInfo authorizationInfo){
        try{
            return ResponseEntity.ok().body(new TokenRequestResponse(userService.getToken(authorizationInfo.getLogin(), authorizationInfo.getPassword()).getToken(),""));
        } catch (WrongPasswordException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<TokenRequestResponse> register(@RequestBody AuthorizationInfo authorizationInfo){
        try{
            return ResponseEntity.ok().body(new TokenRequestResponse(
                userService.register(authorizationInfo.getLogin(),authorizationInfo.getPassword()).getToken(),
                ""
                ));
        } catch (LoginOccupiedException e) {
            return ResponseEntity.badRequest().body(new TokenRequestResponse("",e.getMessage()));
        } catch (PasswordOccupiedException e){
            return ResponseEntity.badRequest().body(new TokenRequestResponse("",e.getMessage()));
        }
    }

    @PostMapping("/sigma")
    public ResponseEntity<SetAdminResponse> makeAdmin(@RequestBody Token token){
        try { 
            userService.makeAdmin(token.getToken());
            return ResponseEntity.ok().body(new SetAdminResponse("Wait, beta. Maybe one sigma would like to help u"));
        } catch (AlreadyAdminException e) {
            return ResponseEntity.badRequest().body(new SetAdminResponse("Somethin goon ron, maybe u shud start mewing?"));
        }
    }
}
