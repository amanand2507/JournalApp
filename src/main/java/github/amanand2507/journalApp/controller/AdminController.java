package github.amanand2507.journalApp.controller;

import github.amanand2507.journalApp.dto.signupDTO;
import github.amanand2507.journalApp.entity.User;
import github.amanand2507.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAll();

        if (all != null && !all.isEmpty()) {
            // Remove password from each user object
            for (User user : all) {
                user.setPassword("");  // or user.setPassword("") if you prefer to set it as an empty string
            }
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public void createUser(@RequestBody signupDTO user) {
        userService.saveAdmin(User.builder().userName(user.getUserName()).password(user.getPassword()).build());
    }

    
}
