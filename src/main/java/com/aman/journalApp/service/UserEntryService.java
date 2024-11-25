package com.aman.journalApp.service;

import com.aman.journalApp.entity.UserEntry;
import com.aman.journalApp.repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserEntryService {

    @Autowired //used to inject the bean automatically.
    private UserEntryRepository userEntryRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveEntry(UserEntry userEntry){
         try {
             userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));

             userEntry.setRoles(Arrays.asList("USER"));
             userEntryRepository.save(userEntry);
         }
         catch (Exception e){
             System.out.println("Hello");
               System.out.println(e);
         }
    }

    public void saveUser(UserEntry userEntry){
        userEntryRepository.save(userEntry);
    }

    public List<UserEntry> getAll(){
        return  userEntryRepository.findAll();
    }

    public Optional<UserEntry> findbyId(ObjectId id){
        return userEntryRepository.findById(id);
    }

    public Optional<UserEntry> deletebyId(ObjectId id){
        userEntryRepository.deleteById(id);
        return Optional.empty();
    }

    public UserEntry findByUserName(String userName){
        return userEntryRepository.findByUserName(userName);
    }
}
