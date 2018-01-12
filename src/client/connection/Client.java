package client.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

import shared.connection.Connection;
import shared.connection.Message;
import shared.security.DiffieHellboy;
import shared.security.Hex;

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
	this.doKeyExchange();
	while(true) {
	    this.logger.info("waiting for input...");
	    String input = this.terminal.nextLine();
	}
    }

    private void doKeyExchange() {
	DiffieHellboy dh = new DiffieHellboy();
	Hex hex = new Hex("sorrybutthatsnosecret");

	this.logger.info("awaiting key...");
	Message publicServerKey = this.connection.read();
	String pServerKey = hex.fromHex(publicServerKey.getData("key"));

	this.logger.info("pkey: " + pServerKey);
    }
}
