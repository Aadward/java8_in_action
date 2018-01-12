package com.syh.jacksonusage.service;

import java.time.LocalDate;
import java.util.Arrays;

import com.syh.jacksonusage.model.Message;
import com.syh.jacksonusage.model.User;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    public Message getMessage() {
        User user = new User(1L, "firstname", "lastname",
                "email", "address", "postalCode",
                "city", "country");
        Message message = new Message(2L, LocalDate.now(), "title",
                user, Arrays.asList(user), "body");

        return message;
    }
}
