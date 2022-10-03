package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAOOrder;
import com.example.warehouse.DAO.DAOProduct;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Product;
import com.example.warehouse.model.ReturnWithMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ProductRestRepository {
    @Autowired
    private DAOProduct daoProduct;
    @Autowired
    private DataSource dataSource;

    public List<Product> getProductsByCategory(long idCategory) {
        List<Product> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoProduct.searchByCategory(connection, idCategory);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Product> getProductsByName(String name) {
        List<Product> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoProduct.searchByName(connection, name);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Product> getProductsByName(String name, double min) {
        List<Product> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoProduct.searchByName(connection, name, min);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Product> getProductsByName(String name, double min, double max) {
        List<Product> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoProduct.searchByName(connection, name, min, max);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Product moreSell() {
        Connection connection = null;
        connection = dataSource.getConnection();
        Product product = null;
        long idProduct = 0;
        try {
            idProduct = DAOProduct.getInstance().searchMoreSell(connection);
            if (idProduct == 0)
                return null;
            product = daoProduct.searchById(connection, idProduct);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public Product moreOrder() {
        Connection connection = null;
        connection = dataSource.getConnection();
        Product product = null;
        long idProduct = 0;
        try {
            idProduct = DAOProduct.getInstance().searchMoreOrder(connection);
            if (idProduct == 0)
                return null;
            product = daoProduct.searchById(connection, idProduct);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public ReturnWithMessage addProduct(String name, String description, double priceSell, int quantity, List<Map<String, Object>> listSupplier) {
        Connection connection = null;
        connection = dataSource.getConnection();
        Product product = null;
        try {
            return daoProduct.insert(connection, new Product(name, description, priceSell, quantity));
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
    }
}
