package com.aman.journalApp.service;

import com.aman.journalApp.entity.UserEntry;
import com.aman.journalApp.repository.UserEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// UserDetailService Implementation to fetch user details for Authentication

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserEntryRepository userEntryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntry user = userEntryRepository.findByUserName(username);
        if(user !=null) {
            UserDetails userDetails = User.builder() // .builder is a method that initiates the builder pattern, allowing for the construction of user-related objects
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
            return userDetails;
        }
        throw  new UsernameNotFoundException("User not found with username" + username);
    }
}
