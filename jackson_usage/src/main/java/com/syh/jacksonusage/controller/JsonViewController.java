package com.syh.jacksonusage.controller;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonView;
import com.syh.jacksonusage.exception.BadParameterException;
import com.syh.jacksonusage.model.Message;
import com.syh.jacksonusage.model.ValidMessage;
import com.syh.jacksonusage.model.Views;
import com.syh.jacksonusage.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
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


    @PostMapping("/valid")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void valid(@RequestBody @Valid ValidMessage message, Errors errors) {
        if (errors.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            errors.getAllErrors().forEach(e -> sb.append(e.getDefaultMessage()).append("\n"));
            throw new BadParameterException(sb.toString());
        }
        System.out.println(message);
    }
}
