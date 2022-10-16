package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAOClientBuy;
import com.example.warehouse.DAO.DAOSupplier;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.ClientBuy;
import com.example.warehouse.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Component
public class SupplierRestRepository {
    @Autowired
    private DAOSupplier daoSupplier;
    @Autowired
    private DataSource dataSource;

    public List<Supplier> getSuppliers() {
        List<Supplier> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoSupplier.searchAll(connection);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Supplier> getSuppliersByName(String name) {
        List<Supplier> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoSupplier.searchByName(connection, name);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean addSupplier(String name, String email, String phone, String api) {
        boolean flag = false;
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            flag = daoSupplier.insert(connection, new Supplier(name, email, phone, api));
        } catch (DAOException e) {
            //throw new RuntimeException(e);
            return false;
        }
        return flag;
    }

    public List<Supplier> getProducts(long idProduct) {
        List<Supplier> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoSupplier.getSupplierByIdProduct(connection, idProduct);
        } catch (Exception e) {
            return null;
        }
        return list;
    }
}
