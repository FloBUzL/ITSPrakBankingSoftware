package server.connection;

import java.util.logging.Logger;

import server.data.Database;
import server.worker.ServerLoginWorker;
import shared.connection.Connection;
import shared.connection.Message;
import shared.security.DiffieHellboy;
import shared.security.Hex;

public class ClientThread extends Thread {
    private Connection connection;
    private Database data;
    private Logger logger;
    private ServerConnectionData connectionData;

    public ClientThread(Connection connection, Database data) {
	this.connectionData = new ServerConnectionData();
	this.connection = connection;
	this.data = data;
	this.logger = Logger.getAnonymousLogger();

	this.connectionData.setConnection(connection);
	this.connectionData.setDatabase(data);

	this.logger.info("ClientThread created");
    }

    public void run() {
	new ServerLoginWorker(this.connectionData).setup().run();

	this.logger.info("started ClientThread");

	while(!this.connection.isClosed()) {
	    break;
	}
    }
}
