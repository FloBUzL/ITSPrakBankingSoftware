package client.connection;

import shared.connection.Connection;
import shared.security.AES;
import shared.superclassifragilistic.ConnectionData;

/**
 * holds all data that's important for the current connection
 * @author Florian
 */
public class ClientConnectionData extends ConnectionData {
    private Terminal terminal = null;
    private Connection connection = null;
    private AES aes = null;
    private String username = null;

    /**
     * gets the current aes object
     * @return the connection's aes object
     */
    public AES getAes() {
        return aes;
    }

    /**
     * sets the aes object
     * @param aes the new aes object
     */
    public void setAes(AES aes) {
        this.aes = aes;
    }

    /**
     * gets the current terminal object
     * @return the terminal object
     */
    public Terminal getTerminal() {
        return terminal;
    }

    /**
     * sets the terminal object
     * @param terminal the new terminal object
     */
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    /**
     * gets the connection to the server
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * sets the connection to the server
     * @param connection the new connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * sets the user's username
     * @param username the user's name
     */
    public void setUsername(String username) {
	this.username = username;
    }

    /**
     * getter for the user's name
     * @return the user's name
     */
    public String getUsername() {
	return this.username;
    }
}
