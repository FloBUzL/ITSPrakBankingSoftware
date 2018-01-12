package server.connection;

import java.util.logging.Logger;

import server.data.Database;
import shared.connection.Connection;
import shared.connection.Message;
import shared.security.DiffieHellboy;
import shared.security.Hex;

public class ClientThread extends Thread {
    private Connection connection;
    private Database data;
    private Logger logger;

    public ClientThread(Connection connection, Database data) {
	this.connection = connection;
	this.data = data;
	this.logger = Logger.getAnonymousLogger();

	this.logger.info("ClientThread created");
    }

    public void run() {
	this.doKeyExchange();

	this.logger.info("started ClientThread");

	while(!this.connection.isClosed()) {
	    break;
	}
    }

    private void doKeyExchange() {
	DiffieHellboy dh = new DiffieHellboy();
	Hex hex = new Hex("sorrybutthatsnosecret");
	dh.createKeyPair();

	String key = new String(dh.getEncodedPublicKey());
	this.logger.info("pkey: " + key);
	key = hex.toHex(key);

	this.logger.info("pkey: " + key);

	Message publicKey = new Message();
	publicKey
		.addData("task", "key_exchange")
		.addData("key", key);

	this.connection.write(publicKey);
    }
}
