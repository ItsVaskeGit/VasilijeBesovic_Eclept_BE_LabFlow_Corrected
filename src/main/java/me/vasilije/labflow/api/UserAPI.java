package me.vasilije.labflow.api;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.dto.request.LoginDTO;
import me.vasilije.labflow.dto.request.RegisterDTO;
import me.vasilije.labflow.service.UserService;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;
    private final TokenUtils utils;
    private final AuthenticationManager manager;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginDTO login) {
        var auth = manager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        return utils.fetchToken(auth);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Boolean register(@RequestBody RegisterDTO register) {
        return userService.registerNewUser(register);
    }


    @RolesAllowed("admin")
    @RequestMapping(path = "/promote/{username}", method = RequestMethod.PUT)
    public Boolean promote(@PathVariable String username) {
        return userService.promote(username);
    }
}
