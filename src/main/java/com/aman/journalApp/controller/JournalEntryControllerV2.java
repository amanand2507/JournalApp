package com.aman.journalApp.controller;

import com.aman.journalApp.entity.JournalEntry;
import com.aman.journalApp.entity.UserEntry;
import com.aman.journalApp.service.JournalEntryService;
import com.aman.journalApp.service.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//@REST Controller(combination of the @controller and @ResponseBody annotation.) :used for making restful web services,used at the class level and allows the class to handle the requests(GET,POST,Delete,PUT) made by the client.
//@RequestMapping : used to map web(HTTP) requests to Spring Controller methods.
@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired //used to inject the bean automatically.
    private JournalEntryService journalEntryService;
    @Autowired
    private UserEntryService userEntryService;
//@GetMapping:  maps HTTP GET requests to a specific handler method in Spring controllers. used to define endpoints of RESTful API and handle various HTTP requests
          @GetMapping
          public ResponseEntity<?> getAllJournalEntriesOfUser(){
              Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
              String userName=authentication.getName();
              UserEntry userEntry =  userEntryService.findByUserName(userName); // Searching userName in DB, userName got from HTTP request
              List<JournalEntry> allData = userEntry.getJournalEntries(); //getting JournalEntry associated with userName
              if(allData != null && !allData.isEmpty()) {

                  return new ResponseEntity<>(allData, HttpStatus.OK);
              }
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
          //@PostMapping:It maps specific URLs to handler methods allowing you to receive and process data submitted through POST requests.
     @PostMapping
     // @RequestBody: Applicable for the incoming request data.Used with POST, PUT, PATCH methods to read the request body
     // ResponseEntity<> : Used to return the HTTP Status
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {

              try {
                  Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
                  String userName=authentication.getName();
                  journalEntryService.saveEntry(myEntry, userName);
                  return new ResponseEntity<>(myEntry,HttpStatus.CREATED);

              } catch(Exception e){
                  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

              }

          }
        @GetMapping("id/{myId}")
        // @PathVariable: to retrieve data from the URL path. you can extract a user ID from a URL like /users/123 and pass it to a method that retrieves the corresponding user’s details.
        public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String userName=authentication.getName();
            UserEntry userEntry = userEntryService.findByUserName(userName);
            List<JournalEntry> collect = userEntry.getJournalEntries().stream().filter(x ->x.getId().equals(myId)).collect(Collectors.toList());
           if (!collect.isEmpty()){
               Optional<JournalEntry> journalEntry = journalEntryService.findbyId(myId);
               if(journalEntry.isPresent()) {
                   return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
               }

           }
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
         }
    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String userName) {
        journalEntryService.deletebyId(myId,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{id}")
     public ResponseEntity<JournalEntry> updateJournalById(
             @PathVariable ObjectId id,
             @RequestBody JournalEntry newEntry,
             @PathVariable String userName
    ){
        JournalEntry oldEntry= journalEntryService.findbyId(id).orElse(null);
       if(oldEntry !=null){
        oldEntry.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
      oldEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());

           journalEntryService.saveEntry(oldEntry);
          return new ResponseEntity<>(oldEntry,HttpStatus.CREATED);
}
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
}
