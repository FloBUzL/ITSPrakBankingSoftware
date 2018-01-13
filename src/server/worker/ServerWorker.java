package server.worker;

import server.connection.ServerConnectionData;
import shared.superclassifragilistic.Worker;

public abstract class ServerWorker extends Worker {
    protected ServerConnectionData connectionData;

    public ServerWorker(ServerConnectionData connectionData) {
	this.connectionData = connectionData;
    }
}
