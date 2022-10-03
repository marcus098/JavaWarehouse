package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DAOOrder {

    private static DAOOrder instance;
    private static final String INSERT = "INSERT INTO ordine(data,costo_totale,descrizione, quantita, id_prodotto_fornitore,arrivato) VALUES (NOW(),?,?,?,?,?)";
    private static final String FIND_BY_ID = "\n" +
            "SELECT * FROM ordine JOIN prodottoFornitore ON prodottoFornitore.id = id_prodotto_fornitore WHERE ordine.id = ? AND arrivato = false LIMIT 1";
    private static final String FIND_BY_CATEGORY = "SELECT * FROM ordine WHERE id_categoria = ? and arrivato = false LIMIT 1";
    //private static final String FIND_ORDERS_MONTHS_QUANTITY = "select date_format(data, '%m') as mese,sum(quantita) as tot from ordine WHERE data>NOW()-INTERVAL 365 DAY AND data < NOW() group by mese;";
    private static final String FIND_ORDERS_MONTHS_TOTAL = "select date_format(data, '%m') as mese,sum(quantita) as quantitaTot, sum(costo_totale) as totale from ordine JOIN prodottoFornitore on prodottoFornitore.id = ordine.id_prodotto_fornitore WHERE data>NOW()-INTERVAL 365 DAY AND data < NOW() group by mese;";
    private static final String FIND_ALL = "select ordine.id, data, costo_totale, id_prodotto,descrizione, quantita, id_prodotto_fornitore, id_fornitore as idFornitore from ordine \n" +
            "join prodottoFornitore on prodottoFornitore.id = ordine.id_prodotto_fornitore where arrivato = false";
    private static final String UPDATE_DESCRIPTION = "UPDATE ordine SET description = ? WHERE id= ? LIMIT 1";
    private static final String DELETE = "DELETE FROM ordine WHERE id = ?";
    private static final String GETPRICE = "SELECT costo_prodotto FROM prodottoFornitore WHERE id = ? LIMIT 1";
    private static final String CONFIRM = "UPDATE ordine SET arrivato = true WHERE id = ?";

    private DAOOrder() {
        super();
    }

    public ReturnWithMessage insert(Connection connection, String description, long idProductSupplier, int quantity) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setDouble(1, getPrice(connection, idProductSupplier) * quantity);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setLong(4, idProductSupplier);
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Ordine aggiunto con successo");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Impossibile inserire l'utente, errore DB");
        }
    }
    public double getPrice(Connection connection, long id) throws DAOException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double price = 0;
        try {
            preparedStatement = connection.prepareStatement(GETPRICE);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                price = resultSet.getDouble("costo_prodotto");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return price;
    }

    public Order searchById(Connection connection, long id) throws DAOException {
        Order order = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //Build formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //Parse String to LocalDateTime
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String description = resultSet.getString("descrizione");
                double total = resultSet.getDouble("costo_totale");
                int quantity = resultSet.getInt("quantita");
                //double total = getPrice(connection, resultSet.getLong("id_prodotto_fornitore")) * quantity;
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("data"), formatter);
                Product product = DAOProduct.getInstance().searchById(connection,resultSet.getLong("id_prodotto"));
                order = new Order(id, description,date,total,quantity,product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'ordine, errore DB");
        }
        return order;
    }

    public List<Order> search(Connection connection, long idCategory) throws DAOException {
        List<Order> list = new ArrayList<>();
        //Map<Order,String> mapReturn = new HashMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_CATEGORY);
            preparedStatement.setLong(1, idCategory);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("descrizione");
                double total = resultSet.getDouble("costo_totale");
                int quantity = resultSet.getInt("quantita");
                //double total = getPrice(connection, resultSet.getLong("id_prodotto_fornitore")) * quantity;
                long id = resultSet.getLong("id");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("data"), formatter);
                Product product = DAOProduct.getInstance().searchById(connection,resultSet.getLong("id_prodotto"));
                long supplier = resultSet.getLong("idFornitore");
                list.add(new Order(id, description,date,total,quantity,product,DAOSupplier.getInstance().searchById(connection,supplier)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'ordine, errore DB");
        }
        return list;
    }

    public List<Statistics> searchOrdersMonthsQuantity(Connection connection) throws DAOException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Statistics> statisticsList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(FIND_ORDERS_MONTHS_TOTAL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int total = resultSet.getInt("totale");
                int month = resultSet.getInt("mese");
                int quantity = resultSet.getInt("quantitaTot");
                statisticsList.add(new Statistics(month, quantity, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'ordine, errore DB");
        }
        return statisticsList;
    }

    public List<Order> search(Connection connection) throws DAOException {
        List<Order> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            preparedStatement = connection.prepareStatement(FIND_ALL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("descrizione");
                double total = resultSet.getDouble("costo_totale");
                int quantity = resultSet.getInt("quantita");
                //double total = getPrice(connection, resultSet.getLong("id_prodotto_fornitore")) * quantity;
                long id = resultSet.getLong("id");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("data"), formatter);
                Product product = DAOProduct.getInstance().searchById(connection,resultSet.getLong("id_prodotto"));
                List<Position> positionList = new ArrayList<>();
                positionList = DAOPosition.getInstance().searchByIdProduct(connection, resultSet.getLong("id_prodotto"));
                product.setPositionList(positionList);
                long supplier = resultSet.getLong("idFornitore");
                list.add(new Order(id, description,date,total,quantity,product,DAOSupplier.getInstance().searchById(connection,supplier)));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'ordine, errore DB");
        }
        return list;
    }

    public void modifyDescription(Connection connection, Order order) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_DESCRIPTION);
            preparedStatement.setString(1, order.getDescription());
            preparedStatement.setLong(2, order.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la descrizione, errore DB");
        }
    }

    public void delete(Connection connection, Order order) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, order.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile eliminare l'ordine, errore DB");
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
            //throw new DAOException("Impossibile eliminare l'ordine, errore DB");
            return false;
        }
    }
    public boolean updateArrived(Connection connection, long id) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(CONFIRM);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new DAOException("Impossibile eliminare l'ordine, errore DB");
            return false;
        }
    }

    public static DAOOrder getInstance() {
        if (instance == null) {
            instance = new DAOOrder();
        }
        return instance;
    }

}
