package server.worker;

import server.connection.ServerConnectionData;
import shared.connection.Message;
import shared.superclassifragilistic.Worker;

/**
 * the worker used for sending the user it's balance
 * 
 * @author Florian
 */
public class ServerBalanceWorker extends ServerWorker {
	/**
	 * the constructor
	 * 
	 * @param connectionData
	 *            the connection's data
	 */
	public ServerBalanceWorker(ServerConnectionData connectionData) {
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
	 * sends the balance
	 */
	public Worker run() throws Exception {
		this.sendBalance();
		return this;
	}

	private void sendBalance() throws Exception {
		String balance = this.connectionData.getAes()
				.encode(this.connectionData.getDatabase().collectBalance(this.connectionData.getUserName()));
		Message balanceMessage = new Message().addData("task", "balance").addData("balance", balance);

		this.connectionData.getConnection().write(balanceMessage);
	}
}
