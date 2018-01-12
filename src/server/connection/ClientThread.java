package server.connection;

import java.util.logging.Logger;

import server.data.Database;
import shared.connection.Connection;

public class ClientThread extends Thread {
    private Connection connection;
    private Database data;
    private Logger logger;

    public ClientThread(Connection connection, Database data) {
	this.connection = connection;
	this.data = data;
	this.logger = Logger.getAnonymousLogger();

	this.logger.info("ClientThread created");
    }

    public void run() {
	this.logger.info("started ClientThread");

	while(!this.connection.isClosed()) {
	    break;
	}
    }
}
