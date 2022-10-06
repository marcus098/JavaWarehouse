package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Position;
import com.example.warehouse.model.Product;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAOPosition {

    private static DAOPosition instance;
    private static final String INSERT = "INSERT INTO posizione(nome,descrizione) VALUES (?,?)";
    private static final String SCOPE_IDENTITY = "select @@identity;";
    private static final String INSERT_WITH_PRODUCT = "INSERT INTO posizione(nome,descrizione, id_prodotto) VALUES (?,?,?)";
    private static final String FIND_BY_ID = "SELECT * FROM posizione WHERE id = ? LIMIT 1";
    private static final String FIND_ALL = "SELECT * FROM posizione";
    private static final String FIND_BY_ID_PRODUCT = "SELECT * FROM posizione WHERE posizione.id_prodotto= ?";
    private static final String FIND_BY_NAME_ID_PRODUCT = "SELECT * FROM posizione JOIN prodotto ON posizione.id_prodotto = prodotto.id WHERE posizione.nome = ? AND posizione.id_prodotto= ? LIMIT 1";
    private static final String FIND_NULL = "SELECT * FROM posizione WHERE posizione.nome = ? AND (id_prodotto is null)";
    private static final String UPDATE_DESCRIPTION = "UPDATE posizione SET descrizione = ? WHERE id= ?";
    private static final String UPDATE_NAME = "UPDATE posizione SET nome = ? WHERE id= ?";
    private static final String UPDATE_PRODUCT = "UPDATE posizione SET id_prodotto = ? WHERE id= ?";
    private static final String UPDATE_PRODUCT_NULL = "UPDATE posizione SET id_prodotto = NULL WHERE id= ?";
    private static final String DELETE = "DELETE FROM posizione WHERE id = ?";

    private DAOPosition() {
        super();
    }

    public long scopeIdentity(Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(SCOPE_IDENTITY);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
               // String name = resultSet.getString("nome");
                return resultSet.getLong("@@identity");
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
            //throw new DAOException("Impossibile inserire la posizione, errore DB");
        }
    }

    public long insert(Connection connection, Position position) throws DAOException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (position.getProduct() != null) {
                preparedStatement = connection.prepareStatement(INSERT_WITH_PRODUCT);
                preparedStatement.setString(1, position.getName());
                preparedStatement.setString(2, position.getDescription());
                preparedStatement.setLong(3, position.getProduct().getId());
            } else {
                preparedStatement = connection.prepareStatement(INSERT);
                preparedStatement.setString(1, position.getName());
                preparedStatement.setString(2, position.getDescription());
            }
            preparedStatement.executeUpdate();
            return scopeIdentity(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean modifyProduct(Connection connection, long idPosition, long idProduct) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_PRODUCT);
            preparedStatement.setLong(1, idProduct);
            preparedStatement.setLong(2, idPosition);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifyProductNull(Connection connection, long idPosition) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_NULL);
            preparedStatement.setLong(1, idPosition);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Position> searchAll(Connection connection) throws DAOException {
        List<Position> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_ALL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String description = resultSet.getString("descrizione");
                String name = resultSet.getString("nome");
                Product product = DAOProduct.getInstance().searchById(connection, resultSet.getLong("posizione.id_prodotto"));
                list.add(new Position(id, name, description, product));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare la posizione, errore DB");
        }
        return list;
    }

    public List<Position> searchByName(Connection connection, String name) throws DAOException {
        List<Position> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_NULL);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String description = resultSet.getString("descrizione");
                Product product = DAOProduct.getInstance().searchById(connection, resultSet.getLong("posizione.id_prodotto"));
                list.add(new Position(id, name, description, product));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare la posizione, errore DB");
        }
        return list;
    }

    public List<Position> searchByName(Connection connection, String name, long idProduct) throws DAOException {
        List<Position> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_NAME_ID_PRODUCT);
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, idProduct);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("posizione.id");
                String description = resultSet.getString("posizione.descrizione");
                Product product = DAOProduct.getInstance().searchById(connection, resultSet.getLong("posizione.id_prodotto"));
                list.add(new Position(id, name, description, product));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare la posizione, errore DB");
        }
        return list;
    }

    public List<Position> searchByIdProduct(Connection connection, long idProduct) throws DAOException {
        List<Position> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID_PRODUCT);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String description = resultSet.getString("descrizione");
                String name = resultSet.getString("nome");
                //Product product = DAOProduct.getInstance().searchById(connection,resultSet.getLong("posizione.id_prodotto"));
                list.add(new Position(id, name, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare la posizione, errore DB");
        }
        return list;
    }

    public Position searchById(Connection connection, long id) throws DAOException {
        Position position = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("nome");
                String description = resultSet.getString("descrizione");
                Product product = DAOProduct.getInstance().searchById(connection, resultSet.getLong("posizione.id_prodotto"));
                position = new Position(id, name, description, product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare la posizione, errore DB");
        }
        return position;
    }

    public void modifyName(Connection connection, Position position) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_NAME);
            preparedStatement.setString(1, position.getName());
            preparedStatement.setLong(2, position.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il nome della posizione, errore DB");
        }
    }

    public void modifyDescription(Connection connection, Position position) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_DESCRIPTION);
            preparedStatement.setString(1, position.getDescription());
            preparedStatement.setLong(2, position.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la descrizione della posizione, errore DB");
        }
    }

    public void delete(Connection connection, Position position) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, position.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile eliminare la posizione, errore DB");
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

    public static DAOPosition getInstance() {
        if (instance == null) {
            instance = new DAOPosition();
        }
        return instance;
    }

}
