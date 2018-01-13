package server.worker;

import server.connection.ServerConnectionData;
import shared.connection.Message;
import shared.superclassifragilistic.Worker;

public class ServerBalanceWorker extends ServerWorker {

    public ServerBalanceWorker(ServerConnectionData connectionData) {
	super(connectionData);
	// TODO Auto-generated constructor stub
    }

    @Override
    public Worker setup() {
	// TODO Auto-generated method stub
	return this;
    }

    @Override
    public Worker run() throws Exception {
	this.sendBalance();
	return this;
    }

    private void sendBalance() throws Exception {
	String balance = this.connectionData.getAes().encode(this.connectionData.getDatabase().collectBalance(this.connectionData.getUserName()));
	Message balanceMessage = new Message()
		.addData("task", "balance")
		.addData("balance", balance);

	this.connectionData.getConnection().write(balanceMessage);
    }
}
