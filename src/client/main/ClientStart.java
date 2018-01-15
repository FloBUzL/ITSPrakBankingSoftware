package client.main;

import client.connection.Client;
import shared.exception.NotEnoughArgumentsExceptions;

/**
 * the entry point for the client
 * 
 * @author Florian
 */
public class ClientStart {
	/**
	 * starts the application
	 * 
	 * @param args
	 *            0: the host to connect to, 1: the socket to connect to
	 * @throws NotEnoughArgumentsExceptions
	 */
	public static void main(String[] args) throws NotEnoughArgumentsExceptions {
		if (args.length < 2) {
			throw new NotEnoughArgumentsExceptions("specify database file and server port");
		}

		String host = args[0];
		int port = Integer.parseInt(args[1]);

		new Client(host, port).run();
	}
}
