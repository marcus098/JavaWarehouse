package com.example.warehouse.service;

import com.example.warehouse.DAO.DAOOrder;
import com.example.warehouse.DAO.DAOUser;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.model.Order;
import com.example.warehouse.model.Position;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.Statistics;
import com.example.warehouse.repository.OrderRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderRestService {

    @Autowired
    private OrderRestRepository orderRestRepository;

    public List<Order> searchOrders(){
        return orderRestRepository.searchOrders();
    }
    public ReturnWithMessage deleteOrder(long id){
        if(orderRestRepository.deleteOrder(id)){
            return new ReturnWithMessage(true, "Ordine eliminato");
        }
        return new ReturnWithMessage(false, "Ordine non eliminato");
    }
    public ReturnWithMessage confirmOrder(long id, long idPosition){
        if (orderRestRepository.confirmOrder(id, idPosition)){
            return new ReturnWithMessage(true, "Ordine confermato");
        }
        return new ReturnWithMessage(false, "Ordine non confermato");
    }
    public ReturnWithMessage confirmOrder(long id, Position position){
        if (orderRestRepository.confirmOrder(id, position))
            return new ReturnWithMessage(true, "Ordine confermato");
        return new ReturnWithMessage(false, "Ordine non confermato");
    }

    public List<Statistics> searchQuantityOrdersMonths() {
        List<Statistics> list = orderRestRepository.searchQuantityOrdersMonths();
        List<Statistics> newList = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            int integ = i;
            Optional<Statistics> optionalStatistics = list.stream().filter(statistics -> statistics.getMonth()==integ).findFirst();
            if(optionalStatistics.isEmpty())
                newList.add(new Statistics(i,0,0));
            else
                newList.add(optionalStatistics.get());
        }
        System.out.println(newList);
        return newList;
    }

    public ReturnWithMessage addOrder(double total, String description, long idProductSupplier, int quantity){
        return orderRestRepository.addOrder(total,description, idProductSupplier, quantity);
    }
}
