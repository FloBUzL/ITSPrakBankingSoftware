package client.worker;

import client.connection.ClientConnectionData;
import shared.connection.Message;
import shared.superclassifragilistic.Worker;

public class ClientBalanceWorker extends ClientWorker {

    public ClientBalanceWorker(ClientConnectionData connectionData) {
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
	this.sendBalanceRequest();
	this.receiveAndPrintBalance();

	return this;
    }

    private void sendBalanceRequest() {
	Message balanceRequest = new Message()
		.addData("task", "balance")
		.addData("message", "request");
	this.connectionData.getConnection().write(balanceRequest);
    }

    private void receiveAndPrintBalance() throws Exception {
	Message balance = this.connectionData.getConnection().read();
	String toPrint = this.connectionData.getAes().decode(balance.getData("balance"));
	this.connectionData.getTerminal().write(toPrint);
    }
}
