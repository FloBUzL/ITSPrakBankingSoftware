package server.connection;

import server.data.Database;
import shared.connection.Connection;
import shared.superclassifragilistic.ConnectionData;

public class ServerConnectionData implements ConnectionData {
    private Database database = null;
    private int userId = -1;
    private Connection connection = null;

    public Database getDatabase() {
        return database;
    }
    public void setDatabase(Database database) {
        this.database = database;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
