package com.syh.jacksonusage.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syh.jacksonusage.model.Message;
import com.syh.jacksonusage.model.Views;
import com.syh.jacksonusage.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jsonview")
public class JsonViewController {

    @Autowired
    private DataService dataService;

    @GetMapping("/summary")
    @JsonView(Views.Summary.class)
    public Message summary() {
        Message message = dataService.getMessage();
        return message;
    }


    @PostMapping("/summary_deserialize")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void constructMessage(@RequestBody Message body) {
        System.out.println(body);
    }

}
