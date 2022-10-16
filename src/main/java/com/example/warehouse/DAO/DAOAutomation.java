package com.example.warehouse.DAO;

import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.*;
import com.example.warehouse.repository.AutomationRestRepository;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAOAutomation {

    private static DAOAutomation instance;
    //private static final String INSERT = "INSERT INTO automazione(name,attivata) VALUES (?,?)";
    private static final String INSERT_DISCOUNT_RULES = "INSERT INTO regoleSconto(percentuale, periodo, tipoPeriodo, id_automazione) VALUES (?,?,?,1)";
    private static final String FIND_BY_ID = "SELECT * FROM automazione WHERE id = ? LIMIT 1";
    private static final String UPDATE = "UPDATE automazione SET attivata = ? WHERE id= ? LIMIT 1";
    private static final String SCOPE_IDENTITY = "select @@identity;";
    private static final String UPDATE_STOCK_RULES = "UPDATE regoleScorte SET numero = ? WHERE id = ? LIMIT 1";
    private static final String UPDATE_DISCOUNT_RULES = "UPDATE regoleSconto SET percentuale = ?, periodo = ?, tipoPeriodo = ? WHERE id = ? LIMIT 1";
    private static final String GET_AUTOMATIONS = "SELECT * FROM automazione";
    private static final String GET_DISCOUNT_RULES = "SELECT * FROM regoleSconto WHERE id_automazione = 1";
    private static final String GET_STOCK_RULES = "SELECT * FROM regoleScorte WHERE id_automazione = 2";
    private static final String DELETE_DISCOUNT_RULE = "DELETE FROM regoleSconto WHERE id = ?";
    /*QUERY PER AUTOMAZIONE ORDINI*/
    /*private static final String GETQUANTITYPERIOD = "select avg(quantita) as media from ordine \n" +
            "inner join prodottoFornitore on prodottoFornitore.id = id_prodotto_fornitore \n" +
            "where id_prodotto = ? \n" +
            "and data > NOW() - INTERVAL ? DAY\n" +
            "and data < NOW() - INTERVAL ? DAY;";*/
    private static final String GETQUANTITYPERIOD = "select avg(quantita) from acquistoCliente inner join acquistoClienteProdotto on acquistoClienteProdotto.id_acquistoCliente = acquistoCliente.id \n" +
            "where id_prodotto = 1\n" +
            "and data > NOW() - INTERVAL 20 DAY\n" +
            "and data < NOW() - INTERVAL 10 DAY;";
    private static final String GETAVERAGEPRICESEUPPLIER = "select avg(costo_totale/quantita) as media from ordine\n" +
            "inner join prodottoFornitore on prodottoFornitore.id = id_prodotto_fornitore \n" +
            "where id_prodotto = ? \n" +
            "and data > NOW() - INTERVAL 60 DAY";
    private static final String GETINTERVALLASTORDER = "select DATEDIFF(NOW(), data) as differenza from ordine inner join prodottoFornitore on prodottoFornitore.id = id_prodotto_Fornitore where id_prodotto = ?  order by data desc limit 1;";
    private static final String GETLASTQUANTITYORDER = "select quantita from ordine inner join prodottoFornitore on prodottoFornitore.id = id_prodotto_Fornitore where id_prodotto = ? order by data desc limit 1;";

    private static String GETMINIMUMPRICE = "select min(costo_prodotto) as minimo from prodottoFornitore where id_prodotto = ?";
    private static String GETQUANTITYSTOCK = "select quantita from prodotto where id = ?";
    private static String GETPRICESELL = "select prezzoVendita from prodotto where id = ?";
    private static final String GETINTERVALLASTSELL = "select DATEDIFF(NOW(), data) as differenza, data from acquistoClienteProdotto \n" +
            "inner join acquistoCliente on acquistoCliente.id = acquistoClienteProdotto.id_acquistoCliente\n" +
            "where id_prodotto = ? \n" +
            "order by data desc limit 1;";

    public int quantityStock(Connection connection, long idProduct) throws DAOException {
        int quantity = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETQUANTITYSTOCK);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int m = resultSet.getInt("quantita");
                if (m != 0)
                    quantity = m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automazione, errore DB");
        }
        return quantity;
    }

    public int getLastQuantityOrder(Connection connection, long idProduct) throws DAOException {
        int quantity = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETLASTQUANTITYORDER);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int m = resultSet.getInt("quantita");
                if (m != 0)
                    quantity = m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automazione, errore DB");
        }
        return quantity;
    }

    public int getIntervalSell(Connection connection, long idProduct) throws DAOException {
        int interval = -1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETINTERVALLASTSELL);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            System.out.println(preparedStatement);
            if (resultSet.next()) {
                interval = resultSet.getInt("differenza");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automazione, errore DB");
        }
        return interval;
    }
    public int getIntervalOrder(Connection connection, long idProduct) throws DAOException {
        int interval = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETINTERVALLASTORDER);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int m = resultSet.getInt("differenza");
                if (m != 0)
                    interval = m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automazione, errore DB");
        }
        return interval;
    }

    public double priceSell(Connection connection, long idProduct) throws DAOException {
        double priceSell = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETPRICESELL);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double m = resultSet.getDouble("prezzoVendita");
                if (m != 0)
                    priceSell = m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automazione, errore DB");
        }
        return priceSell;
    }

    public double searchQuantityByInterval(Connection connection, int initialInterval, int finalInterval, long idProduct) throws DAOException {
        double average = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETQUANTITYPERIOD);
            preparedStatement.setLong(1, idProduct);
            preparedStatement.setInt(2, initialInterval);
            preparedStatement.setInt(3, finalInterval);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double m = resultSet.getDouble("media");
                if (m != 0)
                    average = m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automazione, errore DB");
        }
        return average;
    }
    public double searchAverageSupplier(Connection connection, long idProduct) throws DAOException {
        double average = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETAVERAGEPRICESEUPPLIER);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double m = resultSet.getDouble("media");
                if (m != 0)
                    average = m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automazione, errore DB");
        }
        return average;
    }

    public double getMinimumPrice(Connection connection, long idProduct) throws DAOException {
        double minimum = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETMINIMUMPRICE);
            preparedStatement.setLong(1, idProduct);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double m = resultSet.getDouble("minimo");
                if (m != 0)
                    minimum = m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare l'automazione, errore DB");
        }
        return minimum;
    }

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
            //throw new DAOException("Impossibile inserire la regola, errore DB");
        }
    }

    public ReturnWithMessage insertDiscountRule(Connection connection, DiscountRules discountRules) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT_DISCOUNT_RULES);
            preparedStatement.setDouble(1, discountRules.getPercentage());
            preparedStatement.setInt(2, discountRules.getPeriod());
            preparedStatement.setInt(3, discountRules.getTypePeriod());
            preparedStatement.executeUpdate();
            long value = scopeIdentity(connection);
            System.out.println(value);
            System.out.println(new ReturnWithMessage(true, "aggiunto con successo", value));
            return new ReturnWithMessage(true, "aggiunto con successo", value);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Impossibile inserire la regola, errore DB");
            //throw new DAOException(""Impossibile inserire la categoria, errore DB"");
        }
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
            throw new DAOException("Impossibile modificare l'automazione, errore DB");
        }
    }

    public ReturnWithMessage modify(Connection connection, boolean bool, long id) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setBoolean(1, bool);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Modificato con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare l'automazione, errore DB");
            //return new ReturnWithMessage(false, "Impossibile modificare l'automazione, errore DB");
        }
    }

    public ReturnWithMessage modifyStocksRules(Connection connection, long number, long id) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_STOCK_RULES);
            preparedStatement.setLong(1, number);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Modificato con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la regola, errore DB");
            //return new ReturnWithMessage(false, "Impossibile modificare l'automazione, errore DB");
        }
    }

    public ReturnWithMessage modifyDiscountRules(Connection connection, DiscountRules discountRules) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_DISCOUNT_RULES);
            preparedStatement.setDouble(1, discountRules.getPercentage());
            preparedStatement.setInt(2, discountRules.getPeriod());
            preparedStatement.setInt(3, discountRules.getTypePeriod());
            preparedStatement.setLong(4, discountRules.getId());
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Modificato con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la regola, errore DB");
            //return new ReturnWithMessage(false, "Impossibile modificare l'automazione, errore DB");
        }
    }

    public List<Automation> getAutomations(Connection connection) throws DAOException {
        List<Automation> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GET_AUTOMATIONS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Automation automation;
                long id = resultSet.getLong("id");
                String name = resultSet.getString("nome");
                boolean active = resultSet.getBoolean("attivata");
                if (id == 1) { //sconti
                    automation = new Automation(name, id, active, getDiscountRules(connection));
                } else if (id == 2) {//scorte min / max
                    automation = new Automation(id, name, active, getStockRules(connection));
                } else {
                    automation = new Automation(id, name, active);
                }
                list.add(automation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare la categoria, errore DB");
        }
        return list;
    }

    public List<DiscountRules> getDiscountRules(Connection connection) throws DAOException {
        List<DiscountRules> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GET_DISCOUNT_RULES);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                double percentage = resultSet.getDouble("percentuale");
                int period = resultSet.getInt("periodo");
                int typePeriod = resultSet.getInt("tipoPeriodo");
                list.add(new DiscountRules(id, percentage, period, typePeriod));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare le regole, errore DB");
        }
        return list;
    }

    public List<StocksRules> getStockRules(Connection connection) throws DAOException {
        List<StocksRules> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GET_STOCK_RULES);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                boolean minMax = resultSet.getBoolean("minMax");
                long number = resultSet.getLong("numero");
                list.add(new StocksRules(id, minMax, number));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare le regole, errore DB");
        }
        return list;
    }

    public ReturnWithMessage deleteDiscountRule(Connection connection, long id) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE_DISCOUNT_RULE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Eliminato con successo");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Errore");
        }
    }

    public static DAOAutomation getInstance() {
        if (instance == null) {
            instance = new DAOAutomation();
        }
        return instance;
    }
}
