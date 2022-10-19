package com.example.warehouse.service;

import com.example.warehouse.DAO.DAOAutomation;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Automation;
import com.example.warehouse.model.DiscountRules;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.StocksRules;
import com.example.warehouse.repository.AutomationRestRepository;
import com.example.warehouse.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@Service
public class AutomationRestService {
    @Autowired
    private AutomationRestRepository automationRestRepository;
    @Autowired
    private UserRestRepository userRestRepository;

    public boolean checkToken(String token, int page){
        return userRestRepository.checkToken(token, page).isBool();
    }

    public List<Automation> getAutomations(){
        return automationRestRepository.getAutomations();
    }
    public List<Automation> auto(){
        return automationRestRepository.loadAutomations();
    }

    public ReturnWithMessage addDiscountRule(DiscountRules discountRules) {
        return automationRestRepository.addDiscountRule(discountRules);
    }

    public ReturnWithMessage addStockRule(StocksRules stocksRules) {
        return automationRestRepository.addStockRule(stocksRules);
    }

    public ReturnWithMessage modifyStockRule(long number, long id) {
        return automationRestRepository.modifyStockRule(number, id);
    }

    public ReturnWithMessage modifyDiscountRule(DiscountRules discountRules) {
        return automationRestRepository.modifyDiscountRule(discountRules);
    }

    public void OrderManagement(long idProduct){
        automationRestRepository.OrderManagement(idProduct);
    }
    public ReturnWithMessage removeStockRule(long id) {
        return automationRestRepository.removeStockRule(id);
    }

    public ReturnWithMessage removeDiscountRule(long id) {
        return automationRestRepository.removeDiscountRule(id);
    }

    public ReturnWithMessage activeAutomation(long id) {
        return automationRestRepository.activeAutomation(id);
    }

    public ReturnWithMessage disableAutomation(long id) {
        return automationRestRepository.disableAutomation(id);
    }

    public void DiscountManagement(long idProduct){
        System.out.println(automationRestRepository.DiscountManagement(idProduct));
    }
}
