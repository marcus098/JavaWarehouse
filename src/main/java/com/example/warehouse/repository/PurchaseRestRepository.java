package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAOClientBuy;
import com.example.warehouse.DAO.DAOProduct;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.ClientBuy;
import com.example.warehouse.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseRestRepository {
    @Autowired
    private DAOClientBuy daoClientBuy;
    @Autowired
    private DataSource dataSource;

    public List<ClientBuy> getPurchases(){
        List<ClientBuy> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoClientBuy.search(connection);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public ClientBuy getPurchases(long id){
        ClientBuy clientBuy;
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            clientBuy = daoClientBuy.searchById(connection, id);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return clientBuy;
    }
}
