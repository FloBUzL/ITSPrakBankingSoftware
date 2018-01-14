package server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.data.AuthenticationErrors;
import server.data.Database;
import shared.connection.Connection;
import shared.exception.AuthenticationException;

/**
 * the server object
 * listens to new connections
 * @author Florian
 */
public class Server {
    private ServerSocket socket = null;
    private Database data;
    private AuthenticationErrors authErrors;

    /**
     * constructor, sets up the AuthenticationErrors object
     * @param db the database object
     * @param socket the socket, the socket should listen to
     */
    public Server(Database db, int socket) {
	this.data = db;
	this.authErrors = new AuthenticationErrors();

	try {
	    this.socket = new ServerSocket(socket);
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(0);
	}
    }

    /**
     * listens for new connections and starts ClientThreads
     */
    public void listen() {
	Socket clientSocket;
	String host;
	while(true) {
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
