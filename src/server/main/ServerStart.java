package server.main;

import server.data.Database;
import shared.exception.NotEnoughArgumentsExceptions;

public class ServerStart {
    public static void main(String[] args) throws NotEnoughArgumentsExceptions {
	if(args.length < 2) {
	    throw new NotEnoughArgumentsExceptions("specify database file and server port");
	}

	String databaseFile = args[0];
	String port = args[1];

	Database db = new Database(databaseFile);
    }
}
