package server.connection;

import server.data.AuthenticationErrors;
import server.data.Database;
import shared.connection.Connection;
import shared.security.AES;
import shared.superclassifragilistic.ConnectionData;

public class ServerConnectionData implements ConnectionData {
    private Database database = null;
    private String userName = null;
    private Connection connection = null;
    private AES aes = null;
    private boolean isAuthenticated = false;
    private AuthenticationErrors authErrors = null;

    public AuthenticationErrors getAuthErrors() {
        return authErrors;
    }
    public AES getAes() {
        return aes;
    }
    public void setAes(AES aes) {
        this.aes = aes;
    }
    public Database getDatabase() {
        return database;
    }
    public void setDatabase(Database database) {
        this.database = database;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public boolean isAuthenticated() {
	return this.isAuthenticated;
    }
    public void authenticate() {
	this.isAuthenticated = true;
    }
    public void setAuthErrors(AuthenticationErrors authErrors) {
	this.authErrors = authErrors;
    }
}
