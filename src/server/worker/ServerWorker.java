package server.worker;

import server.connection.ServerConnectionData;
import shared.superclassifragilistic.Worker;

/**
 * abstract worker class of the server
 * 
 * @author Florian
 */
public abstract class ServerWorker extends Worker {
	protected ServerConnectionData connectionData;

	public ServerWorker(ServerConnectionData connectionData) {
		this.connectionData = connectionData;
	}
}
