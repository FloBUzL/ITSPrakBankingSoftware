package client.worker;

import client.connection.ClientConnectionData;
import shared.superclassifragilistic.Worker;

abstract public class ClientWorker extends Worker {
    protected ClientConnectionData connectionData;

    public ClientWorker(ClientConnectionData connectionData) {
	this.connectionData = connectionData;
    }
}
