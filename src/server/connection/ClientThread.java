package server.connection;

import java.util.logging.Logger;

import server.data.Database;
import server.worker.ServerBalanceWorker;
import server.worker.ServerLoginWorker;
import server.worker.ServerTransactionWorker;
import shared.connection.Connection;
import shared.connection.Message;
import shared.exception.NoSuchTaskException;

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
	try {
	    while(!new ServerLoginWorker(this.connectionData).setup().run().isSucceeded()) {};
	    this.logger.info("started ClientThread");

	    while(!this.connection.isClosed()) {
		Message input = this.connection.read();

		switch(input.getData("task")) {
			case "balance" :
			    new ServerBalanceWorker(this.connectionData).setup().run();
			    break;
			case "transaction" :
			    new ServerTransactionWorker(this.connectionData).setup().run();
			    break;
			case "login" :
			default :
			    throw new NoSuchTaskException(input.getData("task"));
		}
	    }
	} catch (Exception e) {
	    this.connection.close();
	    e.printStackTrace();
	}


    }
}
