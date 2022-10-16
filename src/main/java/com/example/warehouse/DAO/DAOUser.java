package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.Role;
import com.example.warehouse.model.Supplier;
import com.example.warehouse.model.User;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAOUser {

    private static DAOUser instance;
    private static final String INSERT = "INSERT INTO utente(nome,cognome,email,telefono, password, id_ruolo) VALUES (?,?,?,?,?,?)";
    private static final String FIND_BY_EMAIL_PASSWORD = "SELECT * FROM utente WHERE email=? AND password=?";
    private static final String GET_ALL = "select utente.id as id, utente.nome as nome, utente.cognome as cognome, email, telefono, id_ruolo, ruolo.nome as nomeRuolo from utente join ruolo on ruolo.id = utente.id_ruolo where utente.id != ?";
    private static final String FIND_BY_ID = "SELECT * FROM utente WHERE id = ? LIMIT 1";
    private static final String UPDATE_EMAIL = "UPDATE utente SET email = ? WHERE id= ? LIMIT 1";
    private static final String UPDATE_PASSWORD = "UPDATE utente SET password = ? WHERE id= ?";
    private static final String UPDATE_NAME = "UPDATE utente SET nome = ? WHERE id= ?";
    private static final String UPDATE_SURNAME = "UPDATE utente SET cognome = ? WHERE id= ?";
    private static final String UPDATE_PHONE = "UPDATE utente SET telefono = ? WHERE id= ?";
    private static final String UPDATE_ALL = "UPDATE utente SET nome = ?, cognome=?, telefono=?,email=? WHERE id= ?";
    private static final String UPDATE_ALL_PASSWORD = "UPDATE utente SET nome = ?, cognome=?, telefono=?,email=?, password=? WHERE id= ?";
    private static final String UPDATE_ROLE = "UPDATE utente SET id_ruolo = ? WHERE id= ?";
    private static final String DELETE = "DELETE FROM utente WHERE id = ?";

    private DAOUser() {
        super();
    }

    public List<User> getUsers(Connection connection, long idUser) {
        List<User> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GET_ALL);
            preparedStatement.setLong(1, idUser);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("nome");
                String surname = resultSet.getString("cognome");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("telefono");
                long idRole = resultSet.getLong("id_ruolo");
                String nameRole = resultSet.getString("nomeRuolo");
                list.add(new User(id, email, name, surname, phone, new Role(idRole, nameRole, "")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println(list);
        return list;
    }

    public void insert(Connection connection, User user) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPhone());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getIdRole());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile inserire l'utente, errore DB");
        }
    }

    public ReturnWithMessage insert(Connection connection, String name, String surname, String email, String phone, String password, long idRole) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, password);
            preparedStatement.setLong(6, idRole);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Impossibile inserire l'utente, errore del server");
            //throw new DAOException("Impossibile inserire l'utente, errore DB");
        }
        return new ReturnWithMessage(true, "Utente inserito con successo");
    }

    public User searchByEmailPassword(Connection connection, String email, String password) throws DAOException {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_PASSWORD);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("nome");
                String surname = resultSet.getString("cognome");
                String phone = resultSet.getString("telefono");
                long id = resultSet.getLong("id");
                Role role = DAORole.getInstance().searchById(connection, resultSet.getLong("id_ruolo"));
                user = new User(id, email, name, surname, phone, role);
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
                String name = resultSet.getString("nome");
                String email = resultSet.getString("email");
                String surname = resultSet.getString("cognome");
                String phone = resultSet.getString("telefono");
                user = new User(id, email, name, surname, phone);
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

    public ReturnWithMessage modifyAll(Connection connection, User user) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_ALL);
            String name, surname, phone, email;
            name = user.getName();
            surname = user.getSurname();
            email = user.getEmail();
            phone = user.getPhone();
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setLong(5, user.getId());
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Modificato con Successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Impossibile modificare i dati dell'utente, errore DB");
            //throw new DAOException("Impossibile modificare i dati dell'utente, errore DB");
        }
    }

    public ReturnWithMessage modifyAllPassword(Connection connection, User user, String password) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_ALL_PASSWORD);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, password);
            preparedStatement.setLong(6, user.getId());
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Modificato con Successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new DAOException("Impossibile modificare i dati dell'utente, errore DB");
            return new ReturnWithMessage(false, "Impossibile modificare i dati dell'utente, errore DB");
        }
    }

    public void modifyEmail(Connection connection, String email, long idUser) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_EMAIL);
            preparedStatement.setString(1, email);
            preparedStatement.setLong(2, idUser);
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

    public void delete(Connection connection, long id) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);
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
