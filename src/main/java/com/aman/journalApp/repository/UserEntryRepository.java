package com.aman.journalApp.repository;
import com.aman.journalApp.entity.UserEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserEntryRepository extends MongoRepository<UserEntry, ObjectId> {
    UserEntry findByUserName(String username);

    void deleteByUserName(String username);
}
