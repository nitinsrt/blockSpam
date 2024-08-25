package com.spamcalls.instahyre.controller;

import com.spamcalls.instahyre.entities.User;
import com.spamcalls.instahyre.models.UserLoginRequestModel;
import com.spamcalls.instahyre.models.UserRegistrationRequestModel;
import com.spamcalls.instahyre.models.UserSearchResult;
import com.spamcalls.instahyre.service.SpamService;
import com.spamcalls.instahyre.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    private final SpamService spamService;

    @Autowired
    public UserController(UserService userService, SpamService spamService) {
        this.userService = userService;
        this.spamService = spamService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloUser(){
        return ResponseEntity.ok("Localhost is running");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody UserRegistrationRequestModel userRegistrationRequestModel){
       String token= userService.registerUser(userRegistrationRequestModel);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequestModel userLoginRequestModel){
        String token = userService.loginUser(userLoginRequestModel);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/search")
    public List<UserSearchResult> searchUsers(@RequestParam String query, @RequestParam Long loggedInUserId) {
        User loggedInUser = userService.findUserById(loggedInUserId); // Assume method exists

        if (query.matches("\\d+")) { // Simple check if query is numeric
            return userService.searchUsersByPhoneNumber(query, loggedInUser);
        } else {
            return userService.searchUsersByName(query, loggedInUser);
        }
    }

    @PostMapping("/markSpam")
    public void markAsSpam(@RequestParam String phoneNumber, @RequestParam Long userId) {
        User user = userService.findUserById(userId); // Assume method exists
        spamService.markSpam(phoneNumber, user);
    }
}
