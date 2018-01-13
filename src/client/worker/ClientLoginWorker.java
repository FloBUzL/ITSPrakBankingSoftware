package client.worker;

import java.security.PublicKey;

import client.connection.ClientConnectionData;
import shared.connection.Message;
import shared.security.AES;
import shared.security.DiffieHellboy;
import shared.security.Hash;
import shared.security.Hex;
import shared.superclassifragilistic.Worker;

public class ClientLoginWorker extends ClientWorker {
    private DiffieHellboy dh;
    private Hex hex;

    public ClientLoginWorker(ClientConnectionData connectionData) {
	super(connectionData);
    }

    @Override
    public Worker setup() {
	this.dh = new DiffieHellboy();
	this.hex = new Hex("sorrybutthatsnosecret");

	return this;
    }

    @Override
    public Worker run() throws Exception {
	byte[] pServerKey;
	PublicKey serverPublicKey;
	String key;
	Message publicKeyMessage = new Message();
	Message publicServerKey = this.connectionData.getConnection().read();
	Message loginMessage = new Message();
	byte[] sharedSecret;
	Message nonceMessage;
	Message loginResponse;
	String nonce;
	String username;
	String password;

	this.debug("received key " + publicServerKey.getData("key"));

	pServerKey = hex.fromHex(publicServerKey.getData("key"));

	this.dh.createKeyPair(pServerKey);
	this.dh.addPKToKeyAgreement(pServerKey);

	key = hex.toHex(this.dh.getEncodedPublicKey());

	publicKeyMessage
		.addData("task", "key_exchange")
		.addData("key", key);

	this.debug("sending client public key to server");

	this.connectionData.getConnection().write(publicKeyMessage);

	sharedSecret = this.dh.generateSecret();

	this.connectionData.setAes(new AES(sharedSecret));

	nonceMessage = this.connectionData.getConnection().read();
	nonce = this.connectionData.getAes().decode(nonceMessage.getData("nonce"));

	this.debug("received nonce: " + nonce);

	this.connectionData.getTerminal().write("enter user name");
	username = this.connectionData.getTerminal().read();

	this.connectionData.getTerminal().write("enter password");
	password = this.connectionData.getTerminal().read();

	this.debug(username + " - " + password);
	this.debug("cr: " + new Hash(username + password + nonce).toString());
	String cr = this.connectionData.getAes().encode(new Hash(username + password + nonce).toString());

	loginMessage
		.addData("task", "login")
		.addData("user", username)
		.addData("cr", cr);

	this.connectionData.getConnection().write(loginMessage);

	loginResponse = this.connectionData.getConnection().read();
	if(loginResponse.getData("message").equals("Login failed")) {
	    this.connectionData.getTerminal().write("Login failed. Try again.");
	} else {
	    this.connectionData.getTerminal().write("Login successful");
	    this.succeeded = true;
	}

	return this;
    }
}
