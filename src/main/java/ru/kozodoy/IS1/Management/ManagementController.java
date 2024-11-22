package ru.kozodoy.IS1.Management;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class AuthorizationInfo {
    String login;
    String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

class TokenRequestResponse {
    String token;
    String message;

    public TokenRequestResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}

class SetAdminResponse {
    String message;

    public SetAdminResponse(String message) {
        this.message = message;
    }

    public SetAdminResponse() {
    }

    public String getMessage() {
        return message;
    }
}

class GetApplicationsResponse {
    List<Application> applications;
    String message;

    public List<Application> getApplications() {
        return applications;
    }

    public String getMessage() {
        return message;
    }

    public GetApplicationsResponse() {
    }

    public GetApplicationsResponse(List<Application> applications, String message) {
        this.applications = applications;
        this.message = message;
    }
}

@RestController
@RequestMapping("/api/management")
public class ManagementController {

    @Autowired
    UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<TokenRequestResponse> getToken(@RequestBody AuthorizationInfo authorizationInfo) {
        try {
            return ResponseEntity.ok().body(new TokenRequestResponse(userService.getToken(authorizationInfo.getLogin(), authorizationInfo.getPassword()).getToken(), ""));
        } catch (WrongPasswordException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<TokenRequestResponse> register(@RequestBody AuthorizationInfo authorizationInfo) {
        try {
            return ResponseEntity.ok().body(new TokenRequestResponse(
                userService.register(authorizationInfo.getLogin(), authorizationInfo.getPassword()).getToken(),
                ""
            ));
        } catch (LoginOccupiedException e) {
            return ResponseEntity.badRequest().body(new TokenRequestResponse("", e.getMessage()));
        } catch (PasswordOccupiedException e) {
            return ResponseEntity.badRequest().body(new TokenRequestResponse("", e.getMessage()));
        }
    }

    @PostMapping("/sigma")
    public ResponseEntity<SetAdminResponse> makeAdmin(@RequestHeader("Authorization") String token) {
        try {
            userService.makeAdminApplication(token.replace("Bearer ", ""));
            return ResponseEntity.ok().body(new SetAdminResponse("Wait, beta. Maybe one sigma would like to help u"));
        } catch (AlreadyAdminException e) {
            return ResponseEntity.badRequest().body(new SetAdminResponse("Something goon ron, maybe u shud start mewing?"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new SetAdminResponse("Bad token"));
        }
    }

    @GetMapping("/applications")
    public ResponseEntity<GetApplicationsResponse> getApplications(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok().body(new GetApplicationsResponse(userService.getApplications(token.replace("Bearer ", "")), "Ok"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new GetApplicationsResponse(null, "Bad token"));
        }
    }

    @PostMapping("/makeAdmin/{id}")
    public void makeAdmin(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            userService.makeAdmin(token.replace("Bearer ", ""), id);
        } catch (BadTokenException | NoSuchElementException e) {

        }
    }
}
