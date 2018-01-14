package client.connection;

import shared.connection.Connection;
import shared.security.AES;
import shared.superclassifragilistic.ConnectionData;

public class ClientConnectionData implements ConnectionData {
    private Terminal terminal = null;
    private Connection connection = null;
    private AES aes = null;
    private String username = null;

    public AES getAes() {
        return aes;
    }
    public void setAes(AES aes) {
        this.aes = aes;
    }
    public Terminal getTerminal() {
        return terminal;
    }
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public void setUsername(String username) {
	this.username = username;
    }
    public String getUsername() {
	return this.username;
    }
}
