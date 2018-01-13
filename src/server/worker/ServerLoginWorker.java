package server.worker;

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
    public Worker run() {
	Message publicKeyMessage = new Message();
	String key;

	this.dh.createKeyPair();
	key = new String(this.dh.getEncodedPublicKey());
	key = hex.toHex(key);

	publicKeyMessage
		.addData("task", "key_exchange")
		.addData("key", key);

	this.connectionData.getConnection().write(publicKeyMessage);

	this.debug("Sent key " + key);

	return this;
    }
}
