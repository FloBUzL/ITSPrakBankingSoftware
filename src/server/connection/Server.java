package server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import server.data.Database;
import shared.connection.Connection;

public class Server {
    private ServerSocket socket = null;
    private Database data;
    private Logger logger;

    public Server(Database db, int socket) {
	this.data = db;
	this.logger = Logger.getAnonymousLogger();

	try {
	    this.socket = new ServerSocket(socket);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    System.exit(0);
	}
    }

    public void listen() {
	while(true) {
	    this.logger.info("waiting for Client connection...");
	    try {
		Socket clientSocket = this.socket.accept();
		new ClientThread(new Connection(clientSocket), this.data).start();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
}
