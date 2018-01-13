package server.worker;

import server.connection.ServerConnectionData;
import shared.connection.Message;
import shared.security.AES;
import shared.security.DiffieHellboy;
import shared.security.Hex;
import shared.security.RandomString;
import shared.superclassifragilistic.Worker;

public class ServerLoginWorker extends ServerWorker {
    private DiffieHellboy dh;
    private Hex hex;

    public ServerLoginWorker(ServerConnectionData connectionData) {
	super(connectionData);
    }

    @Override
    public Worker setup() {
	this.hex = new Hex("sorrybutthatsnosecret");
	this.dh = new DiffieHellboy();
	return this;
    }

    @Override
    public Worker run() throws Exception {
	Message publicKeyMessage = new Message();
	Message publicClientKey;
	Message nonceMessage = new Message();
	Message loginMessage;
	Message loginResponse = new Message();
	String key;
	byte[] pClientKey;
	byte[] sharedSecret;
	String nonce;
	String username;
	String clientCR;
	String serverCR;

	this.dh.createKeyPair();
	key = hex.toHex(this.dh.getEncodedPublicKey());

	publicKeyMessage
		.addData("task", "key_exchange")
		.addData("key", key);

	this.connectionData.getConnection().write(publicKeyMessage);

	publicClientKey = this.connectionData.getConnection().read();
	pClientKey = hex.fromHex(publicClientKey.getData("key"));
	this.dh.addPKToKeyAgreement(pClientKey);

	sharedSecret = this.dh.generateSecret();
	this.connectionData.setAes(new AES(sharedSecret));

	nonce = new RandomString(32).toString();
	this.debug("Nonce: " + nonce);

	nonceMessage
		.addData("task", "send_nonce")
		.addData("nonce", this.connectionData.getAes().encode(nonce));

	this.connectionData.getConnection().write(nonceMessage);

	loginMessage = this.connectionData.getConnection().read();
	clientCR = this.connectionData.getAes().decode(loginMessage.getData("cr"));
	username = loginMessage.getData("user");

	this.debug("got cr: " + clientCR);

	if(this.connectionData.getDatabase().checkCR(username, nonce, clientCR)) {
	    loginResponse
	    	.addData("task", "login")
	    	.addData("message", "Login successful");
	    this.succeeded = true;
	} else {
	    loginResponse
	    	.addData("task", "login")
	    	.addData("message", "Login failed");
	}

	this.connectionData.getConnection().write(loginResponse);

	return this;
    }
}
