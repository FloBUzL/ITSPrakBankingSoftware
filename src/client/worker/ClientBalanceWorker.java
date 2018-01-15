package client.worker;

import client.connection.ClientConnectionData;
import shared.connection.Message;
import shared.superclassifragilistic.Worker;

/**
 * the worker used for retrieving the user's balance
 * 
 * @author Florian
 */
public class ClientBalanceWorker extends ClientWorker {
	/**
	 * the constructor for the worker
	 * 
	 * @param connectionData
	 *            the connection's data object
	 */
	public ClientBalanceWorker(ClientConnectionData connectionData) {
		super(connectionData);
	}

	@Override
	/**
	 * doesn't do anything yet
	 */
	public Worker setup() {
		return this;
	}

	@Override
	/**
	 * starts the worker
	 */
	public Worker run() throws Exception {
		this.sendBalanceRequest();
		this.receiveAndPrintBalance();

		return this;
	}

	private void sendBalanceRequest() {
		Message balanceRequest = new Message().addData("task", "balance").addData("message", "request");
		this.connectionData.getConnection().write(balanceRequest);
	}

	private void receiveAndPrintBalance() throws Exception {
		Message balance = this.connectionData.getConnection().read();
		// receive and print encrypted balance
		String toPrint = this.connectionData.getAes().decode(balance.getData("balance"));
		this.connectionData.getTerminal().write(toPrint);
	}
}
