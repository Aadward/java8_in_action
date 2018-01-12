package com.syh.jacksonusage.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.syh.jacksonusage.model.Message;
import com.syh.jacksonusage.model.Views;
import com.syh.jacksonusage.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jsonview")
public class JsonViewController {

    @Autowired
    private DataService dataService;

    @GetMapping("/summary")
    @JsonView(Views.Summary.class)
    public Message summary() {
        return dataService.getMessage();
    }

}
