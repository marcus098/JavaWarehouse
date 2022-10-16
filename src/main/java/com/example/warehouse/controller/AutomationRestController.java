package com.example.warehouse.controller;

import com.example.warehouse.model.Automation;
import com.example.warehouse.model.DiscountRules;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.StocksRules;
import com.example.warehouse.service.AutomationRestService;
import com.example.warehouse.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class AutomationRestController {
    @Autowired
    private AutomationRestService automationRestService;

    //private UserRestService userRestService;

    /*@PostMapping("/automation")
    public ReturnWithMessage getAutomations(@RequestBody Map<String, String> request){
        if(userRestService.checkToken(request.get("token")) == 1){
            return new ReturnWithMessage(false, "Logout", null);
        }else{
            //check role
        }
        return new ReturnWithMessage(true, "", automationRestService.getAutomations());
    }*/
    @GetMapping("/discount/{id}")
    public void DiscountManagement(@PathVariable("id") long id){
        automationRestService.DiscountManagement(id);
    }

    @PostMapping("/auto")
    public List<Automation> list(@RequestBody Map<String, String> request){
        //automationRestService.getAutomations();
        String token = request.get("userToken").toString();
        System.out.println("token: " + token);
        return automationRestService.auto();
    }

    @PostMapping("/discountRule/add")
    public ReturnWithMessage addDiscountRule(@RequestBody Map<String, Object> request) {
        double percentage = Double.parseDouble(request.get("percentage").toString());
        int period = Integer.parseInt(request.get("period").toString());
        int typePeriod = Integer.parseInt(request.get("typePeriod").toString());
        String token = request.get("userToken").toString();

        ReturnWithMessage r = automationRestService.addDiscountRule(new DiscountRules(percentage, period, typePeriod));
        System.out.println(r);
        return r;
    }

    @PostMapping("/stockRule/add")
    public ReturnWithMessage addStockRule(@RequestBody Map<String, Object> request) {
        String token = request.get("userToken").toString();
        boolean minMax = Boolean.getBoolean(request.get("minMax").toString());
        long number = Integer.parseInt(request.get("number").toString());
        return automationRestService.addStockRule(new StocksRules(minMax, number));
    }
    @PostMapping("/stockRule/modify")
    public ReturnWithMessage modifyStockRule(@RequestBody Map<String, Object> request) {
        String token = request.get("userToken").toString();
        long number = Long.parseLong(request.get("number").toString());
        long id = Long.parseLong(request.get("id").toString());
        return automationRestService.modifyStockRule(number, id);
    }

    @PostMapping("/discountRule/modify")
    public ReturnWithMessage modifyDiscountRule(@RequestBody Map<String, Object> request) {
        String token = request.get("userToken").toString();
        double percentage = Double.parseDouble(request.get("percentage").toString());
        int period = Integer.parseInt(request.get("period").toString());
        int typePeriod = Integer.parseInt(request.get("typePeriod").toString());
        long id = Long.parseLong(request.get("id").toString());
        return automationRestService.modifyDiscountRule(new DiscountRules(id, percentage, period, typePeriod));
    }

    @PostMapping("/stockRule/delete")
    public ReturnWithMessage removeStockRule(@RequestBody Map<String, Object> request) {
        String token = request.get("userToken").toString();
        long id = Long.parseLong(request.get("id").toString());
        return automationRestService.removeStockRule(id);
    }

    @PostMapping("/discountRule/delete")
    public ReturnWithMessage removeDiscountRule(@RequestBody Map<String, Object> request) {
        String token = request.get("userToken").toString();
        long id = Long.parseLong(request.get("id").toString());
        return automationRestService.removeDiscountRule(id);
    }

    @PostMapping("/automation/active")
    public ReturnWithMessage activeAutomation(@RequestBody Map<String, Object> request) {
        String token = request.get("userToken").toString();
        long id = Long.parseLong(request.get("id").toString());
        return automationRestService.activeAutomation(id);
    }

    @PostMapping("/automation/disable")
    public ReturnWithMessage disableAutomation(@RequestBody Map<String, Object> request) {
        String token = request.get("userToken").toString();
        long id = Long.parseLong(request.get("id").toString());
        return automationRestService.disableAutomation(id);
    }

    @GetMapping("/automation/management/{id}")
    public void OrderManagement(@PathVariable("id") long idProduct){
        automationRestService.OrderManagement(idProduct);
    }

}
