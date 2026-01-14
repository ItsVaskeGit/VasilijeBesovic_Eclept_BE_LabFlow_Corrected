package me.vasilije.labflow.api;

import me.vasilije.labflow.dto.LoginDTO;
import me.vasilije.labflow.dto.RegisterDTO;
import me.vasilije.labflow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserAPI {

    private final UserService userService;

    private UserAPI(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginDTO login) {
        var token = userService.login(login.getUsername(), login.getPassword());

        if(token == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return token;
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Boolean register(@RequestBody RegisterDTO register) {

        var registerNewUser = userService.registerNewUser(register.getUsername(), register.getPassword(), register.isTechnician());

        if(registerNewUser) {
            return true;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "/promote/{username}", method = RequestMethod.PUT)
    public Boolean promote(@PathVariable String username) {
        var promote = userService.promote(username);

        if(!promote) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return true;
    }
}
