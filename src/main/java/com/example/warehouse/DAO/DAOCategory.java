package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Category;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.User;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAOCategory {

    private static DAOCategory instance;
    private static final String INSERT = "INSERT INTO categoria(nome,descrizione) VALUES (?,?)";
    private static final String FIND_BY_NAME = "SELECT * FROM categoria WHERE nome=?";
    private static final String FIND_ALL = "SELECT * FROM categoria";
    private static final String FIND_BY_ID = "SELECT * FROM categoria WHERE id = ? LIMIT 1";
    private static final String UPDATE_NAME = "UPDATE categoria SET nome = ? WHERE id= ? LIMIT 1";
    private static final String UPDATE_DESCRIPTION = "UPDATE categoria SET descrizione = ? WHERE id= ?";
    private static final String DELETE = "DELETE FROM categoria WHERE id = ?";

    private DAOCategory() {
        super();
    }

    public ReturnWithMessage insert(Connection connection, Category category) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "aggiunto con successo");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Impossibile inserire la categoria, errore DB");
            //throw new DAOException(""Impossibile inserire la categoria, errore DB"");
        }
    }

    public List<Category> searchByName(Connection connection, String name) throws DAOException {
        List<Category> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String description = resultSet.getString("descrizione");
                list.add(new Category(id, name, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare la categoria, errore DB");
        }
        return list;
    }

    public List<Category> searchAll(Connection connection) throws DAOException {
        List<Category> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_ALL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String description = resultSet.getString("descrizione");
                String name = resultSet.getString("nome");
                list.add(new Category(id, name, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare le categorie, errore DB");
        }
        return list;
    }

    public Category searchById(Connection connection, long id) throws DAOException {
        Category category = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("nome");
                String description = resultSet.getString("descrizione");
                category = new Category(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare la categoria, errore DB");
        }
        return category;
    }

    public void modifyName(Connection connection, Category category) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_NAME);
            preparedStatement.setString(1, category.getName());
            preparedStatement.setLong(2, category.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il nome della categoria, errore DB");
        }
    }

    public void modifyDescription(Connection connection, Category category) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_DESCRIPTION);
            preparedStatement.setString(1, category.getDescription());
            preparedStatement.setLong(2, category.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la descrizione della categoria, errore DB");
        }
    }

    public boolean delete(Connection connection, Category category) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, category.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean delete(Connection connection, long id) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static DAOCategory getInstance() {
        if (instance == null) {
            instance = new DAOCategory();
        }
        return instance;
    }

}
