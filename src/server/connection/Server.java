package server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import server.data.AuthenticationErrors;
import server.data.Database;
import shared.connection.Connection;
import shared.exception.AuthenticationException;

public class Server {
    private ServerSocket socket = null;
    private Database data;
    private Logger logger;
    private AuthenticationErrors authErrors;

    public Server(Database db, int socket) {
	this.data = db;
	this.logger = Logger.getAnonymousLogger();
	this.authErrors = new AuthenticationErrors();

	try {
	    this.socket = new ServerSocket(socket);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    System.exit(0);
	}
    }

    public void listen() {
	Socket clientSocket;
	String host;
	while(true) {
	    this.logger.info("waiting for Client connection...");
	    try {
		clientSocket = this.socket.accept();
		host = clientSocket.getInetAddress().toString();
		if(this.authErrors.isHostBlocked(host)) {
		    clientSocket.close();
		    throw new AuthenticationException();
		}
		new ClientThread(new Connection(clientSocket), this.data, this.authErrors).start();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
}
