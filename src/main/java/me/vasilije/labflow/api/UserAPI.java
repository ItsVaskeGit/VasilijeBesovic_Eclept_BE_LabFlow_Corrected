package me.vasilije.labflow.api;

import me.vasilije.labflow.dto.LoginDTO;
import me.vasilije.labflow.dto.RegisterDTO;
import me.vasilije.labflow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAPI {

    private final UserService userService;

    private UserAPI(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    private String login(@RequestBody LoginDTO login) {
        var token = userService.login(login.getUsername(), login.getPassword());

        if(token == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND).getBody();
        }

        return token;
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    private Boolean register(@RequestBody RegisterDTO register) {
        var registerNewUser = userService.registerNewUser(register.getUsername(), register.getPassword(), register.isTechnician());
        if(registerNewUser) {
            return new ResponseEntity<Boolean>(HttpStatus.CREATED).getBody();
        }

        return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST).getBody();
    }
}
