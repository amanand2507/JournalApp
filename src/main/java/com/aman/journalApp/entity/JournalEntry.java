package com.aman.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Document // used to specify that the class is a MongoDB document.
@Data
@NoArgsConstructor //is a type of constructor in programming that doesn't take any parameters. Its main purpose is to initialize objects with default values.
public class JournalEntry {
    @Id //unique key
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
}
