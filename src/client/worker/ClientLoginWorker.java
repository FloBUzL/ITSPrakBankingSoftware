package client.worker;

import client.connection.ClientConnectionData;
import shared.connection.Message;
import shared.security.AES;
import shared.security.DiffieHellboy;
import shared.security.Hash;
import shared.security.Hex;
import shared.superclassifragilistic.Worker;

/**
 * handles the user's authentication to the server
 * @author Florian
 */
public class ClientLoginWorker extends ClientWorker {
    private DiffieHellboy dh;
    private Hex hex;

    /**
     * the constructor
     * @param connectionData the connection's data
     */
    public ClientLoginWorker(ClientConnectionData connectionData) {
	super(connectionData);
    }

    @Override
    /**
     * sets up the objects needed for key exchange
     */
    public Worker setup() {
	this.dh = new DiffieHellboy();
	this.hex = new Hex("sorrybutthatsnosecret");

	return this;
    }

    @Override
    /**
     * runs the authentication
     */
    public Worker run() throws Exception {
	byte[] pServerKey;
	byte[] sharedSecret;

	Message publicKeyMessage = new Message();
	Message publicServerKey = this.connectionData.getConnection().read();
	Message loginMessage = new Message();
	Message nonceMessage;
	Message loginResponse;

	String key;
	String nonce;
	String username;
	String password;

	// the public key received from the server
	pServerKey = hex.fromHex(publicServerKey.getData("key"));

	// create own keys
	this.dh.createKeyPair(pServerKey);
	this.dh.addPKToKeyAgreement(pServerKey);

	key = hex.toHex(this.dh.getEncodedPublicKey());

	// and send the public key to the server
	publicKeyMessage
		.addData("task", "key_exchange")
		.addData("key", key);

	this.connectionData.getConnection().write(publicKeyMessage);

	// generate the secret and create aes object
	sharedSecret = this.dh.generateSecret();

	this.connectionData.setAes(new AES(sharedSecret));

	// get the nonce, needed for authentication
	nonceMessage = this.connectionData.getConnection().read();
	nonce = this.connectionData.getAes().decode(nonceMessage.getData("nonce"));

	this.connectionData.debug("received nonce: " + nonce);

	this.connectionData.getTerminal().write("enter username");
	username = this.connectionData.getTerminal().read();

	this.connectionData.getTerminal().write("enter password");
	password = this.connectionData.getTerminal().read();

	String cr = this.connectionData.getAes().encode(new Hash(username + password + nonce).toString());

	// send username and challenge-response
	loginMessage
		.addData("task", "login")
		.addData("user", username)
		.addData("cr", cr);

	this.connectionData.getConnection().write(loginMessage);

	// check if the login succeeded
	loginResponse = this.connectionData.getConnection().read();
	if(loginResponse.getData("message").equals("Login failed")) {
	    this.connectionData.getTerminal().write("Login failed. Try again.");
	} else {
	    this.connectionData.getTerminal().write("Login successful");
	    this.connectionData.setUsername(username);
	    this.connectionData.debug("set username: " + username);
	    this.succeeded = true;
	}

	return this;
    }
}
