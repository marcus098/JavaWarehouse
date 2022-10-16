package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAOOrder;
import com.example.warehouse.DAO.DAOPosition;
import com.example.warehouse.DAO.DAOProduct;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderRestRepository {
    @Autowired
    private DAOOrder daoOrder;
    @Autowired
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Order> searchOrders() {
        Connection connection = null;
        connection = dataSource.getConnection();
        List<Order> list = new ArrayList<>();
        try {
            list = daoOrder.search(connection);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Statistics> searchQuantityOrdersMonths(){
        Connection connection = null;
        connection = dataSource.getConnection();
        List<Statistics> statisticsList = new ArrayList<>();
        try {
            statisticsList = daoOrder.searchOrdersMonthsQuantity(connection);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return statisticsList;
    }

    public boolean deleteOrder(long id) {
        Connection connection = null;
        connection = dataSource.getConnection();
        boolean value;
        try {
            value = daoOrder.delete(connection, id);
        } catch (DAOException e) {
            //throw new RuntimeException(e);
            return false;
        }
        return value;
    }

    public boolean confirmOrder(long id, long idPosition) {
        Connection connection = null;
        connection = dataSource.getConnection();
        boolean value = true;
        try {
            Order order = DAOOrder.getInstance().searchById(connection, id);
            if(order.getProduct().getPositionList().stream().filter(position -> position.getId() == idPosition).findFirst().isEmpty()){
                value = DAOPosition.getInstance().modifyProduct(connection, idPosition, order.getProduct().getId());
            }
            if(!value)
                return false;
            System.out.println(order);
            DAOProduct.getInstance().modifyQuantity(connection, order.getProduct().getId(), order.getQuantity(), "sum");
            value = daoOrder.updateArrived(connection, id);
            return value;
        } catch (DAOException e) {
            return false;
            //throw new RuntimeException(e);
        }
    }

    public boolean confirmOrder(long id, Position position) {
        Connection connection = null;
        connection = dataSource.getConnection();
        boolean value = true;
        try {
          long idPosition = 0;
          idPosition = DAOPosition.getInstance().insert(connection, position);
          if(idPosition != 0)
              return confirmOrder(id, idPosition);
        } catch (DAOException e) {
            return false;
        }
        return value;
    }

    public ReturnWithMessage addOrder(String description, long idSupplier, long idProduct, int quantity){
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            return daoOrder.insert(connection, description, idSupplier, idProduct, quantity);
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
    }
}
