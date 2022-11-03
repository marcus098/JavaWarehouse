package com.example.warehouse.controller;
import com.example.warehouse.model.ReturnWithMessage;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface MailService {

    ReturnWithMessage sendMail(@RequestBody Map<String, String> request);
}