package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAOCategory;
import com.example.warehouse.DAO.DAOOrder;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Category;
import com.example.warehouse.model.ReturnWithMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryRestRepository {
    @Autowired
    private DAOCategory daoCategory;
    @Autowired
    private DataSource dataSource;

    public List<Category> getCategories(long idSupplier){
        Connection connection = null;
        connection = dataSource.getConnection();
        List<Category> categoryList = new ArrayList<>();
        try {
           // if(idSupplier==0) {
                categoryList = daoCategory.searchAll(connection, idSupplier);
            /*}else{
                categoryList = daoCategory.searchAll(connection);
            }*/
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return categoryList;
    }

    public ReturnWithMessage addCategory(String name, String description){
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            return daoCategory.insert(connection, new Category(name, description));
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
    }

    public boolean deleteCategory(long id){
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            return daoCategory.delete(connection, id);
        } catch (DAOException e) {
            return false;
        }
    }
}
