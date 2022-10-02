package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Permission;
import com.example.warehouse.model.User;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAOPermission {

    private static DAOPermission instance;
    private static final String INSERT = "INSERT INTO permesso(nome,descrizione) VALUES (?,?)";
    private static final String FIND_BY_ID = "SELECT * FROM permesso WHERE id = ? LIMIT 1";
    private static final String FIND_BY_NAME = "SELECT * FROM permesso WHERE nome = ? LIMIT 1";
    private static final String UPDATE_DESCRIPRION = "UPDATE permesso SET descrizione = ? WHERE id= ? LIMIT 1";
    private static final String UPDATE_NAME = "UPDATE permesso SET nome = ? WHERE id= ?";
    private static final String DELETE = "DELETE FROM permesso WHERE id = ?";

    private DAOPermission() {
        super();
    }

    public void insert(Connection connection, Permission permission) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1,permission.getName());
            preparedStatement.setString(2, permission.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile inserire il permesso, errore DB");
        }
    }

    public List<Permission> searchByName(Connection connection, String name) throws DAOException {
        List<Permission> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                long id = resultSet.getLong("id");
                list.add(new Permission(id, name, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il permesso, errore DB");
        }
        return list;
    }

    public Permission searchById(Connection connection, long id) throws DAOException {
        Permission permission = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("nome");
                String description = resultSet.getString("descrizione");
                permission = new Permission(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare , erroreil permesso DB");
        }
        return permission;
    }

    public void modifyName(Connection connection, Permission permission) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_NAME);
            preparedStatement.setString(1, permission.getName());
            preparedStatement.setLong(2, permission.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il nome del permesso, errore DB");
        }
    }

    public void modifyDescription(Connection connection, Permission permission) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_DESCRIPRION);
            preparedStatement.setString(1, permission.getDescription());
            preparedStatement.setLong(2, permission.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la descrizione del permesso, errore DB");
        }
    }

    public void delete(Connection connection, Permission permission) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, permission.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile eliminare il permesso, errore DB");
        }
    }

    public static DAOPermission getInstance() {
        if (instance == null) {
            instance = new DAOPermission();
        }
        return instance;
    }

}
