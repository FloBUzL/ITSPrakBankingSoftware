package server.connection;

import server.data.AuthenticationErrors;
import server.data.Database;
import shared.connection.Connection;
import shared.security.AES;
import shared.superclassifragilistic.ConnectionData;

/**
 * holds the important data for a connection
 * @author Florian
 */
public class ServerConnectionData extends ConnectionData {
    private Database database = null;
    private String userName = null;
    private Connection connection = null;
    private AES aes = null;
    private boolean isAuthenticated = false;
    private AuthenticationErrors authErrors = null;

    /**
     * getterr for the auth-error object
     * @return instance of the auth-error object
     */
    public AuthenticationErrors getAuthErrors() {
        return authErrors;
    }

    /**
     * getter for the aes object
     * @return the aes object
     */
    public AES getAes() {
        return aes;
    }

    /**
     * setter for the aes object
     * @param aes new aes object
     */
    public void setAes(AES aes) {
        this.aes = aes;
    }

    /**
     * getter for the database object
     * @return the database object
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * setter for the database object
     * @param database new database object
     */
    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * getter for the username
     * @return the username
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * setter for the username
     * @param userName the user's name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getter for the current connection
     * @return the current connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * setter for the current connection
     * @param connection the connection to the client
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * getter for the auth status of the client's device
     * @return true if device is authenticated
     */
    public boolean isAuthenticated() {
	return this.isAuthenticated;
    }

    /**
     * sets the user's device to authenticated
     */
    public void authenticate() {
	this.isAuthenticated = true;
    }

    /**
     * setter for the auth-error object
     * @param authErrors the auth-error object
     */
    public void setAuthErrors(AuthenticationErrors authErrors) {
	this.authErrors = authErrors;
    }
}
