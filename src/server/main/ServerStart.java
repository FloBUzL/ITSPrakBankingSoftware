package server.main;

import server.connection.Server;
import server.data.Database;
import shared.exception.GeneralException;
import shared.exception.NotEnoughArgumentsExceptions;

public class ServerStart {
    public static void main(String[] args) throws Exception {
	if(args.length < 2) {
	    throw new NotEnoughArgumentsExceptions("specify database file and server port");
	}

	String databaseFile = args[0];
	int port = Integer.parseInt(args[1]);

	Database db;
	try {
	    db = new Database(databaseFile);
	} catch (GeneralException e) {
	    throw new GeneralException("couldn't read database file");
	}
	new Server(db,port).listen();
    }
}
