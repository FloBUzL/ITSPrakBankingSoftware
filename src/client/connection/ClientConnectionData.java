package client.connection;

import shared.connection.Connection;
import shared.superclassifragilistic.ConnectionData;

public class ClientConnectionData implements ConnectionData {
    private Terminal terminal = null;
    private Connection connection = null;

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
}
