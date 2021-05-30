package com.cricket46.games.cricketcards.controller;

import com.cricket46.games.cricketcards.model.User;
import com.cricket46.games.cricketcards.services.CricketCardUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin
@RestController
public class CricketCardUserController {

    @Autowired
    CricketCardUserService cricketCardUserService;

    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticateUser(@RequestBody User user) {
        System.out.println("user--"+user);

        User userLoggedIn = cricketCardUserService.authenticateUser(user.getMobile(), user.getPassword());
        System.out.println("userLoggedIn--"+userLoggedIn);
        return new ResponseEntity<User>(userLoggedIn, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<Boolean> createUser(@RequestBody User user){
        Boolean status = cricketCardUserService.addUser(user);
        return new ResponseEntity<Boolean>(status, HttpStatus.OK);
    }
}
