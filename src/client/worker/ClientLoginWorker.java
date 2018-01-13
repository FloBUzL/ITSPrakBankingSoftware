package client.worker;

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
    public Worker run() {
	String pServerKey;
	Message publicServerKey = this.connectionData.getConnection().read();

	this.debug("received key " + publicServerKey.getData("key"));

	pServerKey = hex.fromHex(publicServerKey.getData("key"));

	return this;
    }
}
