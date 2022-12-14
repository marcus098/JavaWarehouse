package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.ReturnWithMessage;
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
    private static final String INSERT = "INSERT INTO fornitore(nome,email,telefono,api) VALUES (?,?,?,?)";
    private static final String FIND_BY_NAME = "SELECT * FROM fornitore WHERE nome like ?";
    private static final String INSERT_PRODUCT_SUPPLIER = "INSERT INTO prodottoFornitore(id_prodotto, id_fornitore, costo_prodotto) VALUES (?,?,?)";
    private static final String FIND_BY_IDPRODUCT = "SELECT fornitore.id as id, costo_prodotto, email, telefono, nome, api FROM fornitore JOIN prodottoFornitore on prodottoFornitore.id_fornitore = fornitore.id WHERE id_prodotto = ?";
    private static final String FIND_ALL = "SELECT * FROM fornitore";
    private static final String FIND_BY_ID = "SELECT * FROM fornitore WHERE id = ? LIMIT 1";
    private static final String UPDATE_NAME = "UPDATE fornitore SET nome = ? WHERE id= ? LIMIT 1";
    private static final String UPDATE_EMAIL = "UPDATE fornitore SET email = ? WHERE id= ?";
    private static final String UPDATE = "UPDATE fornitore SET nome = ?, email = ?, telefono = ?, api = ? WHERE id = ?";
    private static final String UPDATE_TELEFONO = "UPDATE fornitore SET telefono = ? WHERE id= ?";
    private static final String UPDATE_API = "UPDATE fornitore SET api = ? WHERE id= ?";
    private static final String DELETE = "DELETE FROM fornitore WHERE id = ?";

    private DAOSupplier() {
        super();
    }

    public boolean insert(Connection connection, Supplier supplier) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getEmail());
            preparedStatement.setString(3, supplier.getPhone());
            preparedStatement.setString(4, supplier.getApi());
            // System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            //throw new DAOException("Impossibile inserire il fornitore, errore DB");
        }
    }

    public boolean insertProductSupplier(Connection connection, long idProduct, long idSupplier, double price) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SUPPLIER);
            preparedStatement.setLong(1, idProduct);
            preparedStatement.setLong(2, idSupplier);
            preparedStatement.setDouble(3, price);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            //throw new DAOException("Impossibile inserire il fornitore, errore DB");
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
                list.add(new Supplier(id, name, email, phone, api));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il fornitore, errore DB");
        }
        return list;
    }

    public ReturnWithMessage modify(Connection connection, long idSupplier, String name, String email, String phone, String api) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, api);
            preparedStatement.setLong(5, idSupplier);
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Aggiunto con successo");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Impossibile modificare i dati del fornitore, errore DB");
        }
    }

    public List<Supplier> getSupplierByIdProduct(Connection connection, long idProduct) {
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
                String name = resultSet.getString("nome");
                String api = resultSet.getString("api");
                double price = resultSet.getDouble("costo_prodotto");
                list.add(new Supplier(id, name, email, phone, api, price));
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
            preparedStatement.setString(1, "%" + name + "%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String email = resultSet.getString("email");
                String nameSupplier = resultSet.getString("nome");
                String phone = resultSet.getString("telefono");
                String api = resultSet.getString("api");
                list.add(new Supplier(id, nameSupplier, email, phone, api));
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

    public ReturnWithMessage delete(Connection connection, long id) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Eliminato con successo");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Impossibile eliminare il fornitore, errore DB");
           // throw new DAOException("Impossibile eliminare il fornitore, errore DB");
        }
    }

    public static DAOSupplier getInstance() {
        if (instance == null) {
            instance = new DAOSupplier();
        }
        return instance;
    }

}
