package com.aman.journalApp.controller;

import com.aman.journalApp.entity.UserEntry;
import com.aman.journalApp.service.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/health-check")
    public String healthCheck(){ // This method get mapped with the above URL - when end point will hit - this function will be called
        return "ok";
    }

    @Autowired
    private UserEntryService userEntryService;

    @GetMapping
    public List<UserEntry> getAllUsers(){
        return userEntryService.getAll();
    }
    @GetMapping("id/{myId}")
    public Optional<UserEntry> getJournalEntryById(@PathVariable ObjectId myId){
        return userEntryService.findbyId(myId);
    }
    @PostMapping("/create-user")
    public void createUser(@RequestBody UserEntry userEntry){
        userEntryService.saveEntry(userEntry);
    }


}
