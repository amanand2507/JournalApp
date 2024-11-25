package com.aman.journalApp.controller;

import com.aman.journalApp.entity.JournalEntry;
import com.aman.journalApp.entity.UserEntry;
import com.aman.journalApp.repository.UserEntryRepository;
import com.aman.journalApp.service.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserEntryController {

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private UserEntryRepository userEntryRepository;

          @DeleteMapping("id/{myId}")
           public boolean deleteJournalEntryById(@PathVariable ObjectId myId) {
              userEntryService.deletebyId(myId);
              return true;
           }
           @PutMapping
           public ResponseEntity<?> updateJournalById(@RequestBody UserEntry newEntry){
              Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
              String userName=authentication.getName();
            UserEntry userInDb= userEntryService.findByUserName(userName);
                userInDb.setUserName(newEntry.getUserName());
                userInDb.setPassword(newEntry.getPassword());
                userEntryService.saveEntry(userInDb);

               return new ResponseEntity<>(HttpStatus.NO_CONTENT);
           }
@DeleteMapping
    public ResponseEntity<?> deletebyUsername(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        userEntryRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}