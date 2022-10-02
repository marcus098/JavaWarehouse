package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Role;
import com.example.warehouse.model.User;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAORole {

    private static DAORole instance;
    private static final String INSERT = "INSERT INTO role(nome,descrizione) VALUES (?,?)";
    private static final String FIND_BY_NAME = "SELECT * FROM ruolo WHERE nome like %?%";
    private static final String FIND_BY_ID = "SELECT * FROM ruolo WHERE id = ? LIMIT 1";
    private static final String UPDATE_NAME = "UPDATE ruolo SET nome = ? WHERE id= ? LIMIT 1";
    private static final String UPDATE_DESCRIPTION = "UPDATE ruolo SET descrizione = ? WHERE id= ?";
    private static final String DELETE = "DELETE FROM role WHERE id = ?";

    private DAORole() {
        super();
    }

    public void insert(Connection connection, Role role) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, role.getName());
            preparedStatement.setString(2, role.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile inserire il ruolo, errore DB");
        }
    }

    public List<Role> searchByName(Connection connection, String name) throws DAOException {
        List<Role> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("descrizione");
                long id = resultSet.getLong("id");
                list.add(new Role(id, name, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il ruolo, errore DB");
        }
        return list;
    }

    public Role searchById(Connection connection, long id) throws DAOException {
        Role role = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("nome");
                String description = resultSet.getString("descrizione");
                role = new Role(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il ruolo, errore DB");
        }
        return role;
    }

    public void modifyDescription(Connection connection, Role role) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_DESCRIPTION);
            preparedStatement.setString(1, role.getDescription());
            preparedStatement.setLong(2, role.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la descrizione del ruolo, errore DB");
        }
    }

    public void modifyName(Connection connection, Role role) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_NAME);
            preparedStatement.setString(1, role.getName());
            preparedStatement.setLong(2, role.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il nome del ruolo, errore DB");
        }
    }

    public void delete(Connection connection, Role role) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, role.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile eliminare il ruolo, errore DB");
        }
    }

    public static DAORole getInstance() {
        if (instance == null) {
            instance = new DAORole();
        }
        return instance;
    }

}
