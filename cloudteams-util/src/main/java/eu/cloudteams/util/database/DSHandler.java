package eu.cloudteams.util.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Singleton pattern example using Java Enumeration
 */
public enum DSHandler {

    INSTANCE;

    private DataSource datasource = null;
    private Connection connection = null;
    //Datasource for Logging Database
    private DataSource loggerDatasource = null;
    private Connection loggerConnection = null;

    private final Properties prop = new Properties();

    //All fields are readen from datasource.properties file
    private final String DSName;
    private final String DBName;

    private final String LOGGINGDSName;
    private final String LOGGINGDBName;
    private final String DBUser;
    private final String DBPass;
    private final String DBPort;
    private final String Host;
    private final String Profile;
    private final String DBURL;
    private final String LOGGINGDBURL;

    DSHandler() {
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("datasource.properties"));
        } catch (IOException ex) {
            Logger.getLogger(DSHandler.class.getName()).severe("Couldn't read properties file...");
        }
        this.DSName = prop.getProperty("DSName").trim();
        this.LOGGINGDSName = prop.getProperty("LOGGINGDSName").trim();
        this.LOGGINGDBName = prop.getProperty("LOGGINGDBName").trim();
        this.DBName = prop.getProperty("DBName").trim();
        this.DBUser = prop.getProperty("DBUser").trim();
        this.DBPass = prop.getProperty("DBPass").trim();
        this.DBPort = prop.getProperty("DBPort").trim();
        this.Profile = prop.getProperty("Profile").trim();
        this.Host = prop.getProperty("host_deployment_ip").trim();
        this.DBURL = "jdbc:mysql://" + this.Host + ":" + this.DBPort + "/" + this.DBName + "?user=" + this.DBUser + "&password=" + this.DBPass + "&useEncoding=true&characterEncoding=UTF-8&";
        this.LOGGINGDBURL = "jdbc:mysql://" + this.Host + ":" + this.DBPort + "/" + this.LOGGINGDBName + "?user=" + this.DBUser + "&password=" + this.DBPass + "&useEncoding=true&characterEncoding=UTF-8&";

    }

    public Connection getDatasource() {

        if (datasource == null) {
            Logger.getLogger(DSHandler.class.getName()).log(Level.INFO, "Datasource: {0} is empty!", DSName);
            try {
                Context envContext = new InitialContext();
                datasource = (DataSource) envContext.lookup("java:jboss/datasources/" + DSName);
                Logger.getLogger(DSHandler.class.getName()).log(Level.INFO, "Created new  datasource!");
            } catch (NamingException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            //Quick Fix when requesting connection outside the app server
            if (null == datasource) {
                Logger.getLogger(DSHandler.class.getName()).warning("Fail to get a MySQL from AppServer - Datasource is null, requesting a native MySql Connection....");
                return getConnection();
            }
            return datasource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Creates a new connection to MySQL database system
     *
     * @return An instance of MySQL Connection object
     */
    public Connection getConnection() {
        try {

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                this.connection = DriverManager.getConnection(this.DBURL);
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.connection;
    }

    public Connection getLoggerConnection() {
        try {

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                this.loggerConnection = DriverManager.getConnection(this.LOGGINGDBURL);
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.loggerConnection;
    }

    /**
     * Close all the resources used to access a Database
     *
     * @param connection A MySql Connection instance
     * @param preparedStatement A PreparedStatment stream
     * @param resultSet A ResultSet stream
     * @return
     */
    public boolean closeDBStreams(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        boolean status = true;
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
                status = false;
            }
        }
        if (null != preparedStatement) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
                status = false;
            }
        }
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
                status = false;
            }
        }

        if (status) {
            //Logger.getLogger(DSHandler.class.getName()).info("Successfuly closed all Database streams...");
        } else {
            Logger.getLogger(DSHandler.class.getName()).severe("Could not close all Database streams... This may lead to memory leak..");
        }

        return status;
    }

    public boolean rollback(Connection connection) {
        boolean isSuccess = false;
        if (null != connection) {
            try {
                connection.rollback();
                isSuccess = !isSuccess;
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return isSuccess;
    }

    public Connection getLoggerDatasource() {
        if (null == loggerDatasource) {

            Logger.getLogger(DSHandler.class.getName()).log(Level.INFO, "Logger Datasource: {0} is empty!", LOGGINGDSName);
            try {
                Context envContext = new InitialContext();
                loggerDatasource = (DataSource) envContext.lookup("java:jboss/datasources/" + LOGGINGDSName);
                Logger.getLogger(DSHandler.class.getName()).log(Level.INFO, "Created new  datasource!");
            } catch (NamingException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            // Quick Fix when requesting connection outside the app server
            if (null == loggerDatasource) {
                Logger.getLogger(DSHandler.class.getName()).warning("Fail to get a MySQL from AppServer - Datasource is null, requesting a native MySql Connection....");
                return getLoggerConnection();
            }
            return loggerDatasource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        Logger.getLogger(DSHandler.class.getName()).log(Level.INFO, "Current Profile: {0}\nDatabase URL: {1}", new Object[]{DSHandler.INSTANCE.Profile, DSHandler.INSTANCE.DBURL});
        Connection ds = DSHandler.INSTANCE.getDatasource();
        PreparedStatement stm;
        try {
            stm = ds.prepareStatement("SELECT * from Class");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Logger.getLogger(DSHandler.class.getName()).info(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
