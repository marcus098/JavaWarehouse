package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAOPosition;
import com.example.warehouse.DAO.DAOProduct;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Position;
import com.example.warehouse.model.Product;
import com.example.warehouse.model.ReturnWithMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Component
public class PositionRestRepository {
    @Autowired
    private DAOPosition daoPosition;
    @Autowired
    private DataSource dataSource;

    public List<Position> getPositions(){
        List<Position> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoPosition.searchAll(connection);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Position> searchPositionByName(String name){
        List<Position> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoPosition.searchByName(connection,name);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<Position> searchPositionByName(String name, long id){
        List<Position> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoPosition.searchByName(connection,name, id);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public boolean addPosition(Position position){
        Connection connection = null;
        connection = dataSource.getConnection();
        boolean value;
        try {
            value = (daoPosition.insert(connection, position)!=0);
            return value;
        } catch (DAOException e) {
            //throw new RuntimeException(e);
            return false;
        }
    }

    public boolean updateProductPosition(long idPosition, long idProduct){
        Connection connection = null;
        connection = dataSource.getConnection();
        boolean value;
        try {
            value = daoPosition.modifyProduct(connection, idPosition, idProduct);
            return value;
        } catch (DAOException e) {
            //throw new RuntimeException(e);
            return false;
        }
    }
    public boolean updateProductPosition(long idPosition){
        Connection connection = null;
        connection = dataSource.getConnection();
        boolean value;
        try {
            value = daoPosition.modifyProductNull(connection, idPosition);
            return value;
        } catch (DAOException e) {
            //throw new RuntimeException(e);
            return false;
        }
    }

    public List<Position> searchPositionByIdProduct(long idProduct){
        List<Position> list = new ArrayList<>();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            list = daoPosition.searchByIdProduct(connection,idProduct);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean deletePosition(long id){
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            return daoPosition.delete(connection, id);
        } catch (DAOException e) {
            //throw new RuntimeException(e);
            return false;
        }
    }

    public boolean emptyPosition(long id){
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            return daoPosition.modifyProductNull(connection, id);
        } catch (DAOException e) {
            //throw new RuntimeException(e);
            return false;
        }
    }
}
