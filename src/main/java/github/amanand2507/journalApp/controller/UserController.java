package github.amanand2507.journalApp.controller;

import github.amanand2507.journalApp.dto.signupDTO;
import github.amanand2507.journalApp.entity.User;
import github.amanand2507.journalApp.repository.UserRepository;
import github.amanand2507.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Operation(summary = "Update user details", description = "Updates the username and password for the authenticated user.")
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody signupDTO user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);
        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>("Updated",HttpStatus.OK);
    }

    @Operation(summary = "Delete the authenticated user", description = "Deletes the user record for the currently authenticated user.")
    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(authentication.getName());
        userService.deleteById(user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}