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
public class DAOProduct {

    private static DAOProduct instance;
    private static final String MORE_SELL = "SELECT id_prodotto, SUM(quantita) as sum FROM acquistoCliente JOIN acquistoClienteProdotto on acquistoClienteProdotto.id_acquistoCliente = acquistoCliente.id WHERE data>NOW()-INTERVAL 365 DAY AND data < NOW() group by id_prodotto order by sum DESC LIMIT 1;";
    private static final String MORE_ORDER = "select id_prodotto, SUM(ordine.quantita) as sum from ordine JOIN prodottoFornitore ON prodottoFornitore.id = ordine.id_prodotto_fornitore JOIN prodotto ON prodotto.id = prodottoFornitore.id_prodotto WHERE data>NOW()-INTERVAL 365 DAY AND data < NOW() group by id_prodotto order by sum DESC;";
    private static final String INSERT = "INSERT INTO prodotto(nome,quantita,descrizione, prezzoVendita, id_categoria) VALUES (?,?,?,?,?)";
    private static final String FIND_BY_NAME = "SELECT * FROM prodotto WHERE nome like ?";
    private static final String FIND_BY_NAME_SUPPLIER = "SELECT * FROM prodotto JOIN prodottoFornitore ON prodottoFornitore.id_prodotto = prodotto.id WHERE nome like ? AND id_fornitore = ?";
    private static final String FIND_BY_NAME_MIN = "SELECT * FROM prodotto WHERE nome like ? AND prezzoVendita >= ?";
    private static final String FIND_BY_NAME_MIN_SUPPLIER = "SELECT * FROM prodotto JOIN prodottoFornitore ON prodottoFornitore.id_prodotto = prodotto.id WHERE nome like ? AND prezzoVendita >= ? AND id_fornitore = ?";
    private static final String FIND_BY_NAME_MIN_MAX = "SELECT * FROM prodotto WHERE nome like ? AND prezzoVendita >= ? AND prezzoVendita <= ?";
    private static final String FIND_BY_NAME_MIN_MAX_SUPPLIER = "SELECT * FROM prodotto JOIN prodottoFornitore ON prodottoFornitore.id_prodotto = prodotto.id WHERE nome like ? AND prezzoVendita >= ? AND prezzoVendita <= ? AND id_fornitore = ?";
    private static final String FIND_BY_CATEGORY = "SELECT * FROM prodotto WHERE id_categoria = ?";
    private static final String FIND_BY_CATEGORY_SUPPLIER = "SELECT * FROM prodotto JOIN prodottoFornitore ON prodottoFornitore.id_prodotto = prodotto.id WHERE id_categoria = ? AND id_fornitore = ?";
    private static final String FIND_BY_ID = "SELECT * FROM prodotto WHERE id = ? LIMIT 1";
    private static final String UPDATE_NAME = "UPDATE prodotto SET nome = ? WHERE id= ? LIMIT 1";
    private static final String UPDATE_QUANTITY = "UPDATE prodotto SET quantita = ? WHERE id= ?";
    private static final String UPDATE_QUANTITY_SUM = "UPDATE prodotto SET quantita = quantita + ? WHERE id= ?";
    private static final String UPDATE_QUANTITY_MINUS = "UPDATE prodotto SET quantita = quantita - ? WHERE id= ?";
    private static final String UPDATE_DESCRIZIONE = "UPDATE prodotto SET descrizione = ? WHERE id= ?";
    private static final String UPDATE_PREZZO_VENDITA = "UPDATE prodotto SET prezzoVendita = ? WHERE id= ?";
    private static final String UPDATE_POSIZIONE = "UPDATE prodotto SET id_posizione = ? WHERE id= ?";
    private static final String UPDATE_CATEGORIA = "UPDATE prodotto SET id_categoria = ? WHERE id= ?";
    private static final String SCOPE_IDENTITY = "select @@identity;";
    private static final String DELETE = "DELETE FROM prodotto WHERE id = ?";

    private DAOProduct() {
        super();
    }

    public ReturnWithMessage insert(Connection connection, Product product, long idCategory) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getQuantity());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setDouble(4, product.getPriceSell());
            preparedStatement.setLong(5, idCategory);
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Prodotto aggiunto con successo");
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new DAOException("Impossibile inserire il prodotto, errore DB");
            return new ReturnWithMessage(false, "Impossibile inserire il prodotto, errore DB");
        }
    }

    public List<Product> searchByName(Connection connection, String name, long idSupplier) throws DAOException {
        List<Product> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if(idSupplier==0) {
                preparedStatement = connection.prepareStatement(FIND_BY_NAME);
                preparedStatement.setString(1, "%" + name + "%");
            }else{
                preparedStatement = connection.prepareStatement(FIND_BY_NAME_SUPPLIER);
                preparedStatement.setString(1, "%" + name + "%");
                preparedStatement.setLong(2, idSupplier);
            }
           // System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int quantity = resultSet.getInt("quantita");
                String description = resultSet.getString("descrizione");
                String nameProduct = resultSet.getString("nome");
                double priceSell = resultSet.getDouble("prezzoVendita");
                List<Position> positionList = DAOPosition.getInstance().searchByIdProduct(connection, resultSet.getLong("id"));
                Category category = DAOCategory.getInstance().searchById(connection, resultSet.getLong("id_categoria"));
                long id = resultSet.getLong("id");
                list.add(new Product(id, nameProduct, description, priceSell, quantity, positionList, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il prodotto, errore DB");
        }
        return list;
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
            //throw new DAOException("Impossibile cercare ultimo id, errore DB");
        }
    }


    public List<Product> searchByName(Connection connection, String name, double min, long idSupplier) throws DAOException {
        List<Product> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if(idSupplier==0) {
                preparedStatement = connection.prepareStatement(FIND_BY_NAME_MIN);
                preparedStatement.setString(1, "%" + name + "%");
                preparedStatement.setDouble(2, min);
            }else{
                preparedStatement = connection.prepareStatement(FIND_BY_NAME_MIN_SUPPLIER);
                preparedStatement.setString(1, "%" + name + "%");
                preparedStatement.setDouble(2, min);
                preparedStatement.setLong(3, idSupplier);
            }
          //  System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int quantity = resultSet.getInt("quantita");
                String description = resultSet.getString("descrizione");
                String nameProduct = resultSet.getString("nome");
                double priceSell = resultSet.getDouble("prezzoVendita");
                List<Position> positionList = DAOPosition.getInstance().searchByIdProduct(connection, resultSet.getLong("id"));
                Category category = DAOCategory.getInstance().searchById(connection, resultSet.getLong("id_categoria"));
                long id = resultSet.getLong("id");
                list.add(new Product(id, nameProduct, description, priceSell, quantity, positionList, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il prodotto, errore DB");
        }
        return list;
    }

    public List<Product> searchByName(Connection connection, String name, double min, double max, long idSupplier) throws DAOException {
        List<Product> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if(idSupplier==0) {
                preparedStatement = connection.prepareStatement(FIND_BY_NAME_MIN_MAX);
                preparedStatement.setString(1, "%" + name + "%");
                preparedStatement.setDouble(2, min);
                preparedStatement.setDouble(3, max);
            }else{
                preparedStatement = connection.prepareStatement(FIND_BY_NAME_MIN_MAX_SUPPLIER);
                preparedStatement.setString(1, "%" + name + "%");
                preparedStatement.setDouble(2, min);
                preparedStatement.setDouble(3, max);
                preparedStatement.setLong(4, idSupplier);
            }
           // System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int quantity = resultSet.getInt("quantita");
                String description = resultSet.getString("descrizione");
                String nameProduct = resultSet.getString("nome");
                double priceSell = resultSet.getDouble("prezzoVendita");
                List<Position> positionList = DAOPosition.getInstance().searchByIdProduct(connection, resultSet.getLong("id"));
                Category category = DAOCategory.getInstance().searchById(connection, resultSet.getLong("id_categoria"));
                long id = resultSet.getLong("id");
                list.add(new Product(id, nameProduct, description, priceSell, quantity, positionList, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il prodotto, errore DB");
        }
        return list;
    }

    public List<Product> searchByCategory(Connection connection, long idCategory, long idSupplier) throws DAOException {
        List<Product> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if(idSupplier==0) {
                preparedStatement = connection.prepareStatement(FIND_BY_CATEGORY);
                preparedStatement.setLong(1, idCategory);
            }else{
                preparedStatement = connection.prepareStatement(FIND_BY_CATEGORY_SUPPLIER);
                preparedStatement.setLong(1, idCategory);
                preparedStatement.setLong(2, idSupplier);
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int quantity = resultSet.getInt("quantita");
                String name = resultSet.getString("nome");
                String description = resultSet.getString("descrizione");
                double priceSell = resultSet.getDouble("prezzoVendita");
                long id = resultSet.getLong("id");
                List<Position> positionList = DAOPosition.getInstance().searchByIdProduct(connection, resultSet.getLong("id"));
                Category category = DAOCategory.getInstance().searchById(connection, resultSet.getLong("id_categoria"));
                list.add(new Product(id, name, description, priceSell, quantity, positionList, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il prodotto, errore DB");
        }
        return list;
    }

    public Product searchById(Connection connection, long id) throws DAOException {
        Product product = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("nome");
                String description = resultSet.getString("descrizione");
                double priceSell = resultSet.getDouble("prezzoVendita");
                int quantity = resultSet.getInt("quantita");
         //       System.out.println(name);
                List<Position> positionList = new ArrayList<>();
                positionList = DAOPosition.getInstance().searchByIdProduct(connection, resultSet.getLong("id"));
                Category category = DAOCategory.getInstance().searchById(connection, resultSet.getLong("id_categoria"));
                product = new Product(id, name, description, priceSell, quantity, positionList, category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il prodotto, errore DB");
        }
        return product;
    }

    public long searchMoreSell(Connection connection) throws DAOException {
        long idProduct = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(MORE_SELL);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idProduct = resultSet.getInt("id_prodotto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il prodotto, errore DB");
        }
        return idProduct;
    }

    public long searchMoreOrder(Connection connection) throws DAOException {
        long idProduct = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(MORE_ORDER);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idProduct = resultSet.getInt("id_prodotto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile cercare il prodotto, errore DB");
        }
        return idProduct;
    }

    public void modifyDescription(Connection connection, Product product) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_DESCRIZIONE);
            preparedStatement.setString(1, product.getDescription());
            preparedStatement.setLong(2, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la descrizione del prodotto, errore DB");
        }
    }

    public void modifyName(Connection connection, Product product) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_NAME);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setLong(2, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il nome del prodotto, errore DB");
        }
    }

    public void modifyCategory(Connection connection, Product product) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_CATEGORIA);
            preparedStatement.setLong(1, product.getCategory().getId());
            preparedStatement.setLong(2, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la categoria del prodotto, errore DB");
        }
    }

    public void modifySellPrice(Connection connection, Product product) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_PREZZO_VENDITA);
            preparedStatement.setDouble(1, product.getPriceSell());
            preparedStatement.setLong(2, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare il prezzo di vendita del prodotto, errore DB");
        }
    }

    public void modifyQuantity(Connection connection, Product product, int quantity) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(UPDATE_QUANTITY);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setLong(2, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile modificare la quantita' del prodotto, errore DB");
        }
    }

    public boolean modifyQuantity(Connection connection, long idProduct, int quantity, String str) throws DAOException {
        if (str == "sum" || str == "minus") {
            PreparedStatement preparedStatement = null;
            try {
                if (str == "sum")
                    preparedStatement = connection.prepareStatement(UPDATE_QUANTITY_SUM);
                else
                    preparedStatement = connection.prepareStatement(UPDATE_QUANTITY_MINUS);
                preparedStatement.setInt(1, quantity);
                preparedStatement.setLong(2, idProduct);
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean modifyQuantity(Connection connection, Product product, String str) throws DAOException {
        if (str == "sum" || str == "minus") {
            PreparedStatement preparedStatement = null;
            try {
                if (str == "sum")
                    preparedStatement = connection.prepareStatement(UPDATE_QUANTITY_SUM);
                else
                    preparedStatement = connection.prepareStatement(UPDATE_QUANTITY_MINUS);
                preparedStatement.setInt(1, product.getQuantity());
                preparedStatement.setLong(2, product.getId());
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
                //throw new DAOException("Impossibile modificare la quantita' del prodotto, errore DB");
            }
        } else {
            return false;
        }
    }

    public void delete(Connection connection, Product product) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossibile eliminare il prodotto, errore DB");
        }
    }
    public ReturnWithMessage delete(Connection connection, long idProduct) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, idProduct);
            preparedStatement.executeUpdate();
            return new ReturnWithMessage(true, "Eliminato con successo");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Errore DB");
        }
    }

    public static DAOProduct getInstance() {
        if (instance == null) {
            instance = new DAOProduct();
        }
        return instance;
    }

}
