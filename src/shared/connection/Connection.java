package shared.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * wrapper object for the socket object
 * 
 * @author Florian
 */
public class Connection {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	/**
	 * constructor; sets up socket IO
	 * 
	 * @param socket
	 *            the socket to use
	 */
	public Connection(Socket socket) {
		this.socket = socket;

		try {
			this.out = new PrintWriter(socket.getOutputStream());
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * closes the socket
	 */
	public void close() {
		try {
			this.in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.out.close();
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets the client's ip (probably)
	 * 
	 * @return the client's ip
	 */
	public String getIP() {
		return this.socket.getInetAddress().toString();
	}

	/**
	 * checks if the socket is closed
	 * 
	 * @return true if closed
	 */
	public boolean isClosed() {
		return this.socket.isClosed();
	}

	/**
	 * waits for a message
	 * 
	 * @return the message received
	 */
	public Message read() {
		try {
			String line = this.in.readLine();
			return Message.fromString(line);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * sends a message
	 * 
	 * @param msg
	 *            the message to send
	 */
	public void write(Message msg) {
		this.out.println(msg);
		this.out.flush();
	}
}
