package client.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import client.worker.ClientBalanceWorker;
import client.worker.ClientLoginWorker;
import client.worker.ClientTransactionWorker;
import shared.connection.Connection;
import shared.superclassifragilistic.Worker;

public class Client {
    private Connection connection;
    private Logger logger;
    private Terminal terminal;
    private ClientConnectionData connectionData;

    public Client(String host,int port) {
	this.connectionData = new ClientConnectionData();
	this.logger = Logger.getAnonymousLogger();
	this.terminal = new Terminal();

	this.connectionData.setTerminal(this.terminal);

	try {
	    this.connection = new Connection(new Socket(host,port));
	    this.connectionData.setConnection(this.connection);
	    this.logger.info("connected to " + host + " on port " + port);
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    System.exit(0);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    System.exit(0);
	}
    }

    public void run() {
	try {
	    while(!new ClientLoginWorker(this.connectionData).setup().run().isSucceeded()) {}
	    while(true) {
		this.terminal.write("waiting for input... [b] balance [t] transaction [e] exit");
	    	String input = this.terminal.read();

	    	switch(input) {
	    		case "e" :
	    		case "exit" :
	    		case "q" :
	    		case "quit" :
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
