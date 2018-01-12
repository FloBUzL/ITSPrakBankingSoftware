package client.main;

import client.connection.Client;
import shared.exception.NotEnoughArgumentsExceptions;

public class ClientStart {
    public static void main(String[] args) throws NotEnoughArgumentsExceptions {
	if(args.length < 2) {
	    throw new NotEnoughArgumentsExceptions("specify database file and server port");
	}

	String host = args[0];
	int port = Integer.parseInt(args[1]);

	new Client(host,port).run();
    }
}
