package ru.kozodoy.IS1.Management;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.kozodoy.IS1.Services.MinioService;

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
    Boolean isAdmin;

    public TokenRequestResponse(String token, String message, Boolean isAdmin) {
        this.token = token;
        this.message = message;
        this.isAdmin = isAdmin;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
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

class ApplicationDTO {
    private Long id;
    private String userName;
    public ApplicationDTO(Long id, String userName){
        this.id = id;
        this.userName = userName;
    }

    public Long getId(){
        return id;
    }

    public String getUserName(){
        return userName;
    }
}

class GetApplicationsResponse {
    List<ApplicationDTO> applications;
    String message;

    public List<ApplicationDTO> getApplications() {
        return applications;
    }

    public String getMessage() {
        return message;
    }

    public GetApplicationsResponse() {
    }

    public GetApplicationsResponse(List<Application> applications, String message) {
        if(applications != null) {
            this.applications = new LinkedList<>();
            applications.stream().forEach(x->this.applications.add(new ApplicationDTO(x.getId(), x.getUserz().getLogin())));
        }
        this.message = message;
    }
}

@RestController
@RequestMapping("/api/management")
public class ManagementController {

    @Autowired
    UserService userService;

    @Autowired
    MinioService minioService;

    @PostMapping("/auth")
    public ResponseEntity<TokenRequestResponse> getToken(@RequestBody AuthorizationInfo authorizationInfo) {
        try {
            return ResponseEntity.ok().body(
                new TokenRequestResponse(
                    userService.getToken(
                            authorizationInfo.getLogin(),
                            authorizationInfo.getPassword()
                        ).getToken(),
                        "",
                        userService.isAdmin(authorizationInfo.getLogin())));
        } catch (WrongPasswordException e) {
            return ResponseEntity.badRequest().body(new TokenRequestResponse("", e.getMessage(), false));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new TokenRequestResponse("", e.getMessage(), false));
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<TokenRequestResponse> register(@RequestBody AuthorizationInfo authorizationInfo) {
        try {
            return ResponseEntity.ok().body(new TokenRequestResponse(
                userService.register(authorizationInfo.getLogin(), authorizationInfo.getPassword()).getToken(),
                "",
                false
            ));
        } catch (LoginOccupiedException e) {
            return ResponseEntity.badRequest().body(new TokenRequestResponse("", e.getMessage(), false));
        } catch (PasswordOccupiedException e) {
            return ResponseEntity.badRequest().body(new TokenRequestResponse("", e.getMessage(), false));
        }
    }

    @PostMapping("/sigma")
    public ResponseEntity<SetAdminResponse> makeAdmin(@RequestHeader("Authorization") String token) {
        try {
            userService.makeAdminApplication(token.replace("Bearer ", ""));
            return ResponseEntity.ok().body(new SetAdminResponse("Wait, beta. Maybe one sigma would like to help u"));
        } catch (AlreadyAdminException | AlreadyExistException e) {
            return ResponseEntity.badRequest().body(new SetAdminResponse("Something goon ron, maybe u shud start mewing?"));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new SetAdminResponse("Bad token"));
        }
    }

    @GetMapping("/applications")
    public ResponseEntity<GetApplicationsResponse> getApplications(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok().body(
                new GetApplicationsResponse(
                    userService.getApplications(
                        token.replace("Bearer ", "")
                    )
            , "Ok"));
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

    @PostMapping("/reject/{id}")
    public void rejectApplication(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            userService.rejectApplication(token.replace("Bearer ", ""), id);
        } catch (BadTokenException | IllegalArgumentException e) {

        }
    }

    @GetMapping("/exportHistory")
    public ResponseEntity<List<ExportHistory>> getExportHistory(@RequestHeader("Authorization") String token) {
        try {
            // возвращаем ВСЮ информацию о пользователе и истории экспорта
            return ResponseEntity.ok().body(userService.getExportHistory(token.replace("Bearer ", "")));
        } catch (BadTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        try {
            return ResponseEntity.ok().body(new InputStreamResource(minioService.getFile(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
