package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
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
public class DAOSupplier {

    private static DAOSupplier instance;
    private static final String INSERT = "INSERT INTO fornitore(nome,email,telefono,api) VALUES (?,?,?.?)";
    private static final String FIND_BY_NAME = "SELECT * FROM fornitore WHERE nome like ?";
    private static final String FIND_BY_IDPRODUCT = "SELECT * FROM fornitore JOIN prodottoFornitore on prodottoFornitore.id_fornitore = fornitore.id WHERE id_prodotto = ?";
    private static final String FIND_ALL = "SELECT * FROM fornitore";
    private static final String FIND_BY_ID = "SELECT * FROM fornitore WHERE id = ? LIMIT 1";
    private static final String UPDATE_NAME = "UPDATE fornitore SET nome = ? WHERE id= ? LIMIT 1";
    private static final String UPDATE_EMAIL = "UPDATE fornitore SET email = ? WHERE id= ?";
    private static final String UPDATE_TELEFONO = "UPDATE fornitore SET telefono = ? WHERE id= ?";
    private static final String UPDATE_API = "UPDATE fornitore SET api = ? WHERE id= ?";
    private static final String DELETE = "DELETE FROM fornitore WHERE id = ?";

    private DAOSupplier() {
        super();
    }

    public void insert(Connection connection, Supplier supplier) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1,supplier.getName());
            preparedStatement.setString(2,supplier.getEmail());
            preparedStatement.setString(3,supplier.getPhone());
            preparedStatement.setString(4,supplier.getApi());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile inserire il fornitore, errore DB");
        }
    }
    public List<Supplier> searchAll(Connection connection) throws DAOException {
        List<Supplier> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_ALL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String email = resultSet.getString("email");
                String name = resultSet.getString("nome");
                String phone = resultSet.getString("telefono");
                String api = resultSet.getString("api");
                list.add(new Supplier(id,name,email, phone, api));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il fornitore, errore DB");
        }
        return list;
    }

    public List<Supplier> getSupplierByIdProduct(Connection connection, long idProduct){
        List<Supplier> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_IDPRODUCT);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("telefono");
                String name = resultSet.getString("name");
                String api = resultSet.getString("api");
                list.add(new Supplier(id,name,email, phone, api));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
    public List<Supplier> searchByName(Connection connection, String name) throws DAOException {
        List<Supplier> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_NAME);
            preparedStatement.setString(1, "%"+name+"%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("telefono");
                String api = resultSet.getString("api");
                list.add(new Supplier(id,name,email, phone, api));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il fornitore, errore DB");
        }
        return list;
    }

    public Supplier searchById(Connection connection, long id) throws DAOException {
        Supplier supplier = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("nome");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("telefono");
                String api = resultSet.getString("api");
                supplier = new Supplier(id, name, email, phone, api);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il fornitore, errore DB");
        }
        return supplier;
    }

    public void modifyName(Connection connection, Supplier supplier) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_EMAIL);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setLong(2, supplier.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il nome del fornitore, errore DB");
        }
    }

    public void modifyEmail(Connection connection, Supplier supplier) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_EMAIL);
            preparedStatement.setString(1, supplier.getEmail());
            preparedStatement.setLong(2, supplier.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare l'email del fornitore, errore DB");
        }
    }

    public void modifyPhone(Connection connection, Supplier supplier) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_TELEFONO);
            preparedStatement.setString(1, supplier.getPhone());
            preparedStatement.setLong(2, supplier.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il telefono del fornitore, errore DB");
        }
    }

    public void modifyApi(Connection connection, Supplier supplier) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_TELEFONO);
            preparedStatement.setString(1, supplier.getApi());
            preparedStatement.setLong(2, supplier.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare l'api del fornitore, errore DB");
        }
    }

    public void delete(Connection connection, Supplier supplier) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, supplier.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile eliminare il fornitore, errore DB");
        }
    }

    public static DAOSupplier getInstance() {
        if (instance == null) {
            instance = new DAOSupplier();
        }
        return instance;
    }

}
