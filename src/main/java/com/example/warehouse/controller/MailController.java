package com.example.warehouse.controller;

import com.example.warehouse.model.ReturnWithMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MailController {
    @Autowired
    MailService mailService;

    @CrossOrigin
    @PostMapping("/mail")
    public ReturnWithMessage sendEmailMessage(@RequestBody Map<String, String> request) {
        if(request.containsKey("dest") && request.containsKey("mailMessage") && request.containsKey("subject"))
            return mailService.sendMail(request);
        return new ReturnWithMessage(false, "Dati mancanti");
    }
}
