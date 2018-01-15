package server.connection;

import server.data.AuthenticationErrors;
import server.data.Database;
import server.worker.ServerBalanceWorker;
import server.worker.ServerLoginWorker;
import server.worker.ServerTransactionWorker;
import shared.connection.Connection;
import shared.connection.Message;
import shared.exception.AuthenticationException;
import shared.exception.NoSuchTaskException;

/**
 * thread that's created for every client connection handles user requests
 * 
 * @author Florian
 */
public class ClientThread extends Thread {
	private ServerConnectionData connectionData;

	/**
	 * the thread's construcor
	 * 
	 * @param connection
	 *            the connection object, which holds the socket to the client
	 * @param data
	 *            the database object
	 * @param authErrors
	 *            the auth errors object
	 */
	public ClientThread(Connection connection, Database data, AuthenticationErrors authErrors) {
		this.connectionData = new ServerConnectionData();

		this.connectionData.setConnection(connection);
		this.connectionData.setDatabase(data);
		this.connectionData.setAuthErrors(authErrors);

		this.connectionData.log("ClientThread created");
	}

	/**
	 * runs a loop that gets the client's requests
	 */
	public void run() {
		try {
			// logs in the client
			while (!new ServerLoginWorker(this.connectionData).setup().run().isSucceeded()) {
			}
			;

			while (!this.connectionData.getConnection().isClosed()) {
				// error if the user mustn't connect anymore
				if (this.connectionData.getAuthErrors().isUserBlocked(this.connectionData.getUserName())
						|| this.connectionData.getAuthErrors()
								.isHostBlocked(this.connectionData.getConnection().getIP())) {
					throw new AuthenticationException();
				}
				Message input = this.connectionData.getConnection().read();

				switch (input.getData("task")) {
				case "balance":
					new ServerBalanceWorker(this.connectionData).setup().run();
					break;
				case "transaction":
					new ServerTransactionWorker(this.connectionData).setup().run();
					break;
				case "login":
				default:
					throw new NoSuchTaskException(input.getData("task"));
				}
			}
		} catch (Exception e) {
			this.connectionData.getConnection().close();
			e.printStackTrace();
		}
	}
}
