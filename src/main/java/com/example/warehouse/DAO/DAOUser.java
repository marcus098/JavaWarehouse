package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.User;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DAOUser {

    private static DAOUser instance;
    private static final String INSERT = "INSERT INTO user(name,email,password) VALUES (?,?,?)";
    private static final String FIND_BY_EMAIL_PASSWORD = "SELECT * FROM user WHERE email=? AND password=?";
    private static final String FIND_BY_ID = "SELECT * FROM user WHERE id = ? LIMIT 1";
    private static final String UPDATE_EMAIL = "UPDATE user SET email = ? WHERE id= ? LIMIT 1";
    private static final String UPDATE_PASSWORD = "UPDATE user SET password = ? WHERE id= ?";
    private static final String UPDATE_NAME = "UPDATE user SET name = ? WHERE id= ?";
    private static final String DELETE = "DELETE FROM user WHERE id = ?";

    private DAOUser() {
        super();
    }

    public void insert(Connection connection, User user) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile inserire l'utente, errore DB");
        }
    }

    public User searchByEmailPassword(Connection connection, String email, String password) throws DAOException {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        System.out.println("entro");
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_PASSWORD);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                long id = resultSet.getLong("id");
                user = new User(id, email, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'utente, errore DB");
        }
        return user;
    }

    public User searchById(Connection connection, long id) throws DAOException {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                user = new User(id, email, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'utente, errore DB");
        }
        return user;
    }

    public void modifyEmail(Connection connection, User user) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_EMAIL);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setLong(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare l'email dell'utente, errore DB");
        }
    }

    public void modifyPassword(Connection connection, User user) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_PASSWORD);
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setLong(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la password dell'utente, errore DB");
        }
    }

    public void modifyName(Connection connection, User user) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_NAME);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il nome dell'utente, errore DB");
        }
    }

    public void delete(Connection connection, User user) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile eliminare l'utente, errore DB");
        }
    }

    public static DAOUser getInstance() {
        if (instance == null) {
            instance = new DAOUser();
        }
        return instance;
    }

}
