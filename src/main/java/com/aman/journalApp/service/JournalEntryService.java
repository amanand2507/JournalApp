package com.aman.journalApp.service;

import com.aman.journalApp.entity.JournalEntry;
import com.aman.journalApp.entity.UserEntry;
import com.aman.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired //used to inject the bean automatically.
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserEntryService userEntryService;

    //@Transactional ensures that a method runs within a transactional context. If any exception occurs within the method, the transaction is rolled back.
    public void saveEntry(JournalEntry journalEntry, String userName){
       try {
           UserEntry userEntry = userEntryService.findByUserName(userName);
           JournalEntry saved = journalEntryRepository.save(journalEntry);
           userEntry.getJournalEntries().add(saved);
           //userEntry.setUserName(null); // used for testing transactional annotation
           userEntryService.saveUser(userEntry);

       } catch (Exception e){
           System.out.println(e);
           throw new RuntimeException("An error occurred while saving the entry",e);
       }

    }
    public void saveEntry(JournalEntry journalEntry){

       journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getAll(){
        return  journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findbyId(ObjectId id){
        return journalEntryRepository.findById(id);
    }
    public void deletebyId(ObjectId id, String userName){
        UserEntry userEntry = userEntryService.findByUserName(userName);
        userEntry.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userEntryService.saveEntry(userEntry);
        journalEntryRepository.deleteById(id);
    }

}
// controller -> service(Business Logics) -> repository