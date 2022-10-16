package com.example.warehouse.repository;

import com.example.warehouse.service.AutomationRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataRestRepository {

    @Autowired
    AutomationRestService automationRestService;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent ready) {
        automationRestService.getAutomations();
    }

}
