package client.worker;

import client.connection.ClientConnectionData;
import shared.superclassifragilistic.Worker;

/**
 * the abstract worker class for the client
 * 
 * @author Florian
 */
abstract public class ClientWorker extends Worker {
	protected ClientConnectionData connectionData;

	public ClientWorker(ClientConnectionData connectionData) {
		this.connectionData = connectionData;
	}
}
