package client.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

import client.worker.ClientLoginWorker;
import shared.connection.Connection;
import shared.connection.Message;
import shared.security.DiffieHellboy;
import shared.security.Hex;

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
	    new ClientLoginWorker(this.connectionData).setup().run();
	    while(true) {
		this.logger.info("waiting for input...");
	    	String input = this.terminal.read();
	    }
	} catch(Exception e) {
	    e.printStackTrace();
	    this.connection.close();
	}

    }
}
