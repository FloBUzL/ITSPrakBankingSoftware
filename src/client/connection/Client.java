package client.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

import shared.connection.Connection;

public class Client {
    private Connection connection;
    private Logger logger;
    private Scanner terminal;

    public Client(String host,int port) {
	this.logger = Logger.getAnonymousLogger();
	this.terminal = new Scanner(System.in);

	try {
	    this.connection = new Connection(new Socket(host,port));
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
	while(true) {
	    this.logger.info("waiting for input...");
	    String input = this.terminal.nextLine();
	}
    }
}
