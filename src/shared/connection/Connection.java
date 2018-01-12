package shared.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Connection(Socket socket) {
	this.socket = socket;
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
	    return Message.fromString(this.in.readLine());
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

    public void write(Message line) {
	this.out.println(line);
    }

    public boolean isClosed() {
	return this.socket.isClosed();
    }
}
