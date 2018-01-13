package client.worker;

import java.security.PublicKey;

import client.connection.ClientConnectionData;
import shared.connection.Message;
import shared.security.DiffieHellboy;
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
	byte[] sharedSecret;

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

	this.debug("ss: " + this.hex.toHex(sharedSecret));

	return this;
    }
}
