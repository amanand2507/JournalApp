package github.amanand2507.journalApp.controller;

import github.amanand2507.journalApp.dto.signupDTO;
import github.amanand2507.journalApp.entity.User;
import github.amanand2507.journalApp.utilis.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import github.amanand2507.journalApp.service.UserDetailsServiceImpl;
import github.amanand2507.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
@Slf4j
@Tag(name = "Authentication", description = "Authentication related endpoints")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

//    @GetMapping("/health-check")
//    public String healthCheck() {
//        return "Ok";
//    }

    @PostMapping("/signup")
    public  ResponseEntity<String> signup(@RequestBody signupDTO user) {
        userService.saveNewUser(User.builder().userName(user.getUserName()).password(user.getPassword()).build());
        return new ResponseEntity<>("Sign Up Successful Continue to login",HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody signupDTO user) {
        Map<String,String> res = new HashMap<>();

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            res.put("token",jwt);
            res.put("message","logged In Success");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            res.put("message","Incorrect username or password");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }
}
