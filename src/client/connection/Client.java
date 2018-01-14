package client.connection;

import java.net.Socket;

import client.worker.ClientBalanceWorker;
import client.worker.ClientLoginWorker;
import client.worker.ClientTransactionWorker;
import shared.connection.Connection;

/**
 * listens to the terminal and delegates commands
 * @author Florian
 */
public class Client {
    private Connection connection;
    private Terminal terminal;
    private ClientConnectionData connectionData;

    /**
     * connects to the server
     * @param host the server's address
     * @param port the port the server's service listens to
     */
    public Client(String host,int port) {
	this.connectionData = new ClientConnectionData();
	this.terminal = new Terminal();

	this.connectionData.setTerminal(this.terminal);

	try {
	    this.connection = new Connection(new Socket(host,port));
	    this.connectionData.setConnection(this.connection);
	    this.terminal.write("connected to banking server");
	} catch (Exception e) {
	    this.terminal.write("could not connect to server");
	}
    }

    /**
     * executes the login process and listens for commands
     */
    public void run() {
	try {
	    // login
	    while(!new ClientLoginWorker(this.connectionData).setup().run().isSucceeded()) {}
	    while(true) {
		this.terminal.write("waiting for input... [b] balance [t] transaction [e] exit");
	    	String input = this.terminal.read();

	    	switch(input) {
	    		case "e" :
	    		case "exit" :
	    		case "q" :
	    		case "quit" :
	    		case "logout" :
	    		    this.terminal.write("closing connection");
	    		    this.connection.close();
	    		    return;
	    		case "b" :
	    		case "balance" :
	    		    new ClientBalanceWorker(this.connectionData).setup().run();
	    		    break;
	    		case "t" :
	    		case "transaction" :
	    		    new ClientTransactionWorker(this.connectionData).setup().run();
	    		    break;
	    		default :
	    		    this.terminal.write("command not supported");
	    	}
	    }
	} catch(Exception e) {
	    this.terminal.write("Something went wrong, sorry.");
	    this.connection.close();
	}

    }
}
