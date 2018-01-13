package server.worker;

import java.security.NoSuchAlgorithmException;

import server.connection.ServerConnectionData;
import shared.connection.Message;
import shared.security.DiffieHellboy;
import shared.security.Hex;
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
	String key;
	byte[] pClientKey;
	byte[] sharedSecret;

	this.dh.createKeyPair();
	key = hex.toHex(this.dh.getEncodedPublicKey());

	publicKeyMessage
		.addData("task", "key_exchange")
		.addData("key", key);

	this.connectionData.getConnection().write(publicKeyMessage);

	this.debug("Sent key " + new String(this.dh.getEncodedPublicKey()));

	publicClientKey = this.connectionData.getConnection().read();
	this.debug("received public key from client");
	pClientKey = hex.fromHex(publicClientKey.getData("key"));
	this.dh.addPKToKeyAgreement(pClientKey);

	sharedSecret = this.dh.generateSecret();
	this.debug("ss: " + this.hex.toHex(sharedSecret));

	return this;
    }
}
