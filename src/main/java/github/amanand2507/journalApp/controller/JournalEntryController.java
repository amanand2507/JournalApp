package github.amanand2507.journalApp.controller;

import github.amanand2507.journalApp.dto.JournalDTO;
import github.amanand2507.journalApp.entity.JournalEntry;
import github.amanand2507.journalApp.entity.User;
import github.amanand2507.journalApp.service.JournalEntryService;
import github.amanand2507.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@Tag(name = "journal", description = "Journal related endpoints")
@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get All", description = "Get All Journals for the currently authenticated user.")
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Operation(summary = "Add Entry", description = "Add new Journal for the currently authenticated user.")
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalDTO myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            JournalEntry newEntry =  JournalEntry.builder().title(myEntry.title).content(myEntry.content).build();
            journalEntryService.saveEntry(newEntry, userName);
            System.out.println(myEntry);
            System.out.println(userName);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @Operation(summary = "Get Journal by Id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String id) {
        ObjectId myId2 = new ObjectId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId2);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Operation(summary = "Delete Journal by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String id) {
        ObjectId myId = new ObjectId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryService.deleteById(myId, username);
        if (removed) {
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Update Journal by Id")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable String id, @RequestBody JournalDTO newEntry) {
        ObjectId myId = new ObjectId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}