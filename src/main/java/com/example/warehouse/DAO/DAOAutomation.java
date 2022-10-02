package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Automation;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DAOAutomation {

    private static DAOAutomation instance;
    //private static final String INSERT = "INSERT INTO automazione(name,attivata) VALUES (?,?)";
    private static final String FIND_BY_ID = "SELECT * FROM automatizzazione WHERE id = ? LIMIT 1";
    private static final String UPDATE = "UPDATE automatizzazione SET attivata = ? WHERE id= ? LIMIT 1";
    //private static final String DELETE = "DELETE FROM user WHERE id = ?";

    private DAOAutomation() {
        super();
    }

    public Automation searchById(Connection connection, long id) throws DAOException {
        Automation automation = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("nome");
                boolean active = resultSet.getBoolean("attivata");
                automation = new Automation(id, name, active);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automatizzazione, errore DB");
        }
        return automation;
    }

    public void modify(Connection connection, Automation automation) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setBoolean(1, automation.isActive());
            preparedStatement.setLong(2, automation.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare l'automatizzazione, errore DB");
        }
    }

    public static DAOAutomation getInstance() {
        if (instance == null) {
            instance = new DAOAutomation();
        }
        return instance;
    }
}
