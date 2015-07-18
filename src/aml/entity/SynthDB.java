/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.entity;

import aml.global.Config;
import aml.global.Enums.PersistenceMode;
import aml.graph.MyNode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * DB/File csv manager create synthetic database
 *
 * @author ddefalco
 */
public class SynthDB {

    private final String DB_DRIVER;
    private final String DB_CONNECTION;
    private final String DB_USER;
    private final String DB_PASSWORD;

    private final String HEADER_TRANSACTION_FILE = " ID, MONTH, YEAR, ZSCORE_H, ZSCORE_F, LAUNDERER_PARENTS, LAUNDERER_PARTNERS, LAUNDERER_DUMMIES, HONEST \n";
    private final String ROW_TRANSACTION_FILE = " %s, %s, %s, %s, %s, %s, %s, %s, %s \n";

    private final String HEADER_ENTITY_FILE = " ID, TYPE, HONEST, FRAUD \n";
    private final String ROW_ENTITY_FILE = " %s, %s, %s, %s \n";

    //Writer of transaction and nodes
    protected BufferedWriter bwt, bwp;
    //Mode of persistence
    protected PersistenceMode mode;
    protected Connection dbConnection;
    
    protected String filename;

    /**
     * Synthetic DataBase constructor
     */
    public SynthDB() {
        this.mode = Config.instance().getPersistenceMode();
        this.DB_DRIVER = Config.instance().getDataBaseDriver();
        this.DB_CONNECTION = Config.instance().getDataBaseConnection();
        this.DB_USER = Config.instance().getDataBaseUsername();
        this.DB_PASSWORD = Config.instance().getDataBasePassword();
        switch (mode) {
            case FILE:
                try {
                    createFiles();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case DATABASE:
                dbConnection = initDBConnection();
                cleanTables();
                break;
            case ALL:
                try {
                    createFiles();
                    dbConnection = initDBConnection();
                    cleanTables();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }

    /**
     * Write an Entity (person or factory)
     *
     * @param n node to write an entity on file/data base/both
     */
    public synchronized void writeEntity(MyNode n) {        
        switch (mode) {
            case FILE: {
                try {
                    writeEntityFile(n);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case DATABASE: {
                try {
                    insertEntityIntoTable(n);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case ALL: {
                try {
                    writeEntityFile(n);
                    insertEntityIntoTable(n);
                } catch (SQLException | IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Write a Transaction
     *
     * @param t Transaction to write to file/data base/both
     */
    public synchronized void writeTransaction(Transaction t) {
        switch (mode) {
            case FILE: {
                try {
                    writeTransactionFile(t);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case DATABASE: {
                try {
                    insertTransactionIntoTable(t);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case ALL: {
                try {
                    writeTransactionFile(t);
                    insertTransactionIntoTable(t);
                } catch (SQLException | IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Insert a Transaction in DB table
     *
     * @param t Transaction to write to data base
     * @throws java.sql.SQLException
     */
    public void insertTransactionIntoTable(Transaction t) throws SQLException {
        PreparedStatement preparedStatement = null;
        String insertTableSQL = "INSERT INTO TRANSACTIONS"
                + "(ID, MONTH, YEAR_, ZSCORE_H, Z_SCORE_F, LAUNDERER_PARENTS, LAUNDERER_PARTNERS, LAUNDERER_DUMMIES, HONEST) VALUES"
                + "(?,?,?,?,?,?,?,?)";
        try {
            dbConnection.setAutoCommit(true);
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, t.getId());            
            preparedStatement.setShort(2, (short) (t.getMonth() + 1));
            preparedStatement.setShort(3, t.getYear());
            preparedStatement.setDouble(4, t.getAmount());            
            preparedStatement.setDouble(5, t.getZScoreHonest());
            preparedStatement.setDouble(6, t.getZScoreLaunderer());
            preparedStatement.setShort(7, (short)(t.getExistLaundererParents()?1:0));
            preparedStatement.setShort(8, (short)(t.getExistLaundererPartners()?1:0));
            preparedStatement.setShort(9, (short)(t.getExistLaundererDummies()?1:0));
            preparedStatement.setString(10, t.getHonest());
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    /**
     * Cancel DB tables before begin to insert record
     */
    private void cleanTables() {
        try {
            Statement stTruncate1 = dbConnection.createStatement();
            stTruncate1.executeUpdate("TRUNCATE TABLE TRANSACTIONS");

            Statement stTruncate2 = dbConnection.createStatement();
            stTruncate2.executeUpdate("TRUNCATE TABLE ENTITIES");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create files for writing:
     * Entities and Transactions
     */
    private void createFiles() throws IOException {
        /*Create the entities file*/
        long _time = System.currentTimeMillis();
        File filep = new File("." + File.separator + "dbfiles" + File.separator + String.format(Config.instance().getFileNameEntity(), _time));
        FileWriter fwp = new FileWriter(filep.getAbsoluteFile(), true);
        bwp = new BufferedWriter(fwp);
        bwp.write(HEADER_ENTITY_FILE);
        /*Create the transaction file*/
        this.filename = "." + File.separator + "dbfiles" + File.separator + String.format(Config.instance().getFileNameTransaction(), _time);
        File filet = new File("." + File.separator + "dbfiles" + File.separator + String.format(Config.instance().getFileNameTransaction(), _time));
        FileWriter fwt = new FileWriter(filet.getAbsoluteFile(), true);
        bwt = new BufferedWriter(fwt);
        bwt.write(HEADER_TRANSACTION_FILE);
    }

    /**
     * Write Transaction to file
     *
     * @param t Transaction to write on csv file
     */
    private void writeTransactionFile(Transaction t) throws IOException {
        if (bwt != null) {
            bwt.write(String.format(ROW_TRANSACTION_FILE, t.getId(), (t.getMonth() + 1), t.getYear(), t.getZScoreHonest(), t.getZScoreLaunderer(), (t.getExistLaundererParents()?1:0), (t.getExistLaundererPartners()?1:0), (t.getExistLaundererDummies()?1:0), t.getHonest()));
        }
    }

    /**
     * Write Entity to file
     *
     * @param n Node rapresents the Entity to write
     */
    private void writeEntityFile(MyNode n) throws IOException {
        if (bwp != null) {
            bwp.write(String.format(ROW_ENTITY_FILE, n.getId(), n.getType(), (n.isHonest()) ? "YES" : "NO", n.getFraudPotential()));
        }
    }

    /**
     * Insert Entity in data base table
     *
     * @param n Node rapresents the Entity
     */
    private void insertEntityIntoTable(MyNode n) throws SQLException {
        PreparedStatement preparedStatement = null;
        String insertTableSQL = "INSERT INTO ENTITIES"
                + "(ID, TYPE, HONEST, FRAUD) VALUES"
                + "(?,?,?,?)";
        try {
            dbConnection.setAutoCommit(true);
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, n.getId());
            preparedStatement.setString(2, n.getType().name());
            preparedStatement.setString(3, (n.isHonest()) ? "YES" : "NO");
            preparedStatement.setDouble(4, n.getFraudPotential());
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    /**
     * Close files
     */
    private void closeFiles() throws IOException {
        if (bwt != null) {
            bwt.close();
        }
        if (bwp != null) {
            bwp.close();
        }
    }

    /**
     * Close data base Connection
     */
    private void closeDB() throws SQLException {
        if (dbConnection != null) {
            dbConnection.close();
        }
    }

    /**
     * Close the synthetic file writer
     */
    public void close() {
        switch (mode) {
            case FILE: {
                try {
                    closeFiles();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case DATABASE: {
                try {
                    closeDB();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case ALL: {
                try {
                    closeFiles();
                    closeDB();
                } catch (IOException | SQLException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
        }
    }

    /**
     * Initialize the data base Connection
     */
    private Connection initDBConnection() {
        try {
            Class.forName(DB_DRIVER);
            dbConnection = DriverManager.getConnection(
                    DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(SynthDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbConnection;
    }

    public String getTransactionFilename() {
        return filename;
    }        
}
