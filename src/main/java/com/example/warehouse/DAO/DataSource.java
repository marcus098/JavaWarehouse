package com.example.warehouse.DAO;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//Qui i metodi non rilanciano eccezioni ma andrebbe fatto!
@Component
public class DataSource {

    private static Properties dbProperties;
    private static String url;
    private static Driver dbDriver;
    private static DataSource instance;

    private DataSource() {
        super();
        this.load();
    }

    private void load() {
        try {
            dbProperties = new Properties();
            System.out.println(DataSource.class.getProtectionDomain().getCodeSource().getLocation().getPath());

            InputStream inputStream = DataSource.class.getClassLoader().getResourceAsStream("./jdbc.properties");
            //System.out.println(inputStream);
            dbProperties.load(inputStream);
            dbDriver = (Driver) Class.forName(dbProperties.getProperty("DriverClassName")).newInstance();
            //dbDriver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            url = dbProperties.getProperty("url");
           // url = "jdbc:mysql://localhost:3306/warehouse?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = dbDriver.connect(url, dbProperties);
            /*Properties p = new Properties();
            p.setProperty("user", "root");
            p.setProperty("password", "Marcus98!");
            connection = dbDriver.connect(url, p);*/
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return connection;
    }

    public void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
