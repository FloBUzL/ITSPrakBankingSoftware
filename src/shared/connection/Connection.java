package shared.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class Connection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Logger logger;

    public Connection(Socket socket) {
	this.socket = socket;
	this.logger = Logger.getAnonymousLogger();

	try {
	    this.out = new PrintWriter(socket.getOutputStream());
	    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    System.exit(0);
	}
    }

    public Message read() {
	try {
	    this.logger.info("waiting for message...");
	    String line = this.in.readLine();
	    this.logger.info("received message " + line);
	    return Message.fromString(line);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

    public void write(Message line) {
	this.out.println(line);
	this.out.flush();
    }

    public boolean isClosed() {
	return this.socket.isClosed();
    }

    public void close() {
	try {
	    this.in.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	this.out.close();
	try {
	    this.socket.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
