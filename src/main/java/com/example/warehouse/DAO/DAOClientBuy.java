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
import java.util.List;
@Component
public class DAOClientBuy {
    private static DAOClientBuy instance;
    private static final String INSERT = "INSERT INTO acquistoCliente(data,descrizione) VALUES (NOW(),?)";
    private static final String INSERT_PRODUCTS = "INSERT INTO acquistoClienteProdotto(id_acquistoCliente, id_prodotto, prezzoProdotto, quantita, sconto) VALUES (?,?,?,?,0)";

    private static final String FIND_BY_DATE = "SELECT * FROM acquistoCliente JOIN acquistoClienteProdotto ON acquistoClienteProdotto.id_cliente = acquistoProdotto.id WHERE data>=? AND data<=?";
    private static final String FIND_PRODUCTS = "SELECT * FROM acquistoClienteProdotto where id_acquistoCliente = ?";
    private static final String SCOPE_IDENTITY = "select @@identity;";
    private static final String FIND_BY_ID = "SELECT * FROM acquistoCliente JOIN acquistoClienteProdotto ON acquistoClienteProdotto.id_acquistoCliente = acquistoCliente.id WHERE acquistoCliente.id = ? LIMIT 1";
    //private static final String FIND_ALL = "SELECT * FROM acquistoCliente JOIN acquistoClienteProdotto ON acquistoClienteProdotto.id_acquistoCliente = acquistoCliente.id LIMIT 1";
    private static final String FIND_ALL = "SELECT * FROM acquistoCliente";
    private static final String UPDATE_DESCRIPTION = "UPDATE acquistoCliente SET email = ? WHERE id= ? LIMIT 1";
    private static final String DELETE = "DELETE FROM acquistoCliente WHERE id = ?";
    private static final String FIND_SELLS_MONTHS_TOTAL = "select date_format(data, '%m') as mese,sum(quantita) as quantitaTot, sum(prezzoProdotto*quantita*((100-sconto)/100)) as totale \n" +
            "from acquistocliente inner join acquistoclienteprodotto on acquistocliente.id = acquistoclienteprodotto.id_acquistoCliente\n" +
            "WHERE data>NOW()-INTERVAL 365 DAY AND data < NOW() group by mese;";

    private DAOClientBuy() {
        super();
    }

    public boolean insert(Connection connection, String description) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, description);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Statistics> searchSellsMonthsQuantity(Connection connection) throws DAOException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Statistics> statisticsList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(FIND_SELLS_MONTHS_TOTAL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int total = resultSet.getInt("totale");
                int month = resultSet.getInt("mese");
                int quantity = resultSet.getInt("quantitaTot");
                statisticsList.add(new Statistics(month, quantity, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare le statistiche di vendita, errore DB");
        }
        return statisticsList;
    }
    public boolean insertProducts(Connection connection, List<Cart> cart) throws DAOException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        long l = 0;
        try {
            preparedStatement = connection.prepareStatement(SCOPE_IDENTITY);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // String name = resultSet.getString("nome");
                l = resultSet.getLong("@@identity");
            }
            for (Cart c:cart) {
                preparedStatement = connection.prepareStatement(INSERT_PRODUCTS);
                preparedStatement.setLong(1, l);
                preparedStatement.setLong(2, c.getId());
                preparedStatement.setDouble(3, c.getPrice());
                preparedStatement.setInt(4, c.getQuantity());
                preparedStatement.executeUpdate();
                boolean value = DAOProduct.getInstance().modifyQuantity(connection,c.getId(),c.getQuantity(),"minus");
                if(value==false)
                    return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new DAOException("Impossibile inserire l'acquisto, errore DB");
            return false;
        }
    }

    public List<ProductPrice> searchProducts(Connection connection, long idClientBuy) throws DAOException {
        List<ProductPrice> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            preparedStatement = connection.prepareStatement(FIND_PRODUCTS);
            preparedStatement.setLong(1, idClientBuy);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int quantity = resultSet.getInt("quantita");
                double discount = resultSet.getDouble("sconto");
                double priceSell = resultSet.getDouble("prezzoProdotto");
                long id = resultSet.getLong("id");
                Product product = DAOProduct.getInstance().searchById(connection, resultSet.getLong("id_prodotto"));
                list.add(new ProductPrice(id, product, priceSell, discount, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il prodotto, errore DB");
        }
        return list;
    }

    public List<ClientBuy> search(Connection connection) throws DAOException {
        List<ClientBuy> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            preparedStatement = connection.prepareStatement(FIND_ALL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //double discount = resultSet.getDouble("sconto");
                long id = resultSet.getLong("acquistoCliente.id");
                String description = resultSet.getString("descrizione");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("data"), formatter);
                //Product product = DAOProduct.getInstance().searchById(connection, resultSet.getLong("id_prodotto"));
                list.add(new ClientBuy(id, description, searchProducts(connection, id), date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'ordine, errore DB");
        }
        return list;
    }

    public ClientBuy searchById(Connection connection, long id) throws DAOException {
        ClientBuy clientBuy = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double discount = resultSet.getDouble("sconto");
                String description = resultSet.getString("descrizione");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("data"), formatter);
                Product product = DAOProduct.getInstance().searchById(connection, resultSet.getLong("id_prodotto"));
                clientBuy = new ClientBuy(id, description, searchProducts(connection, id), date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'acquisto, errore DB");
        }
        return clientBuy;
    }

    public void delete(Connection connection, ClientBuy clientBuy) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, clientBuy.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile eliminare acquisto, errore DB");
        }
    }

    public static DAOClientBuy getInstance() {
        if (instance == null) {
            instance = new DAOClientBuy();
        }
        return instance;
    }
}
