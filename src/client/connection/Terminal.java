package client.connection;

import java.util.Scanner;

/**
 * Terminal object that wraps IO operations on the system in/out
 * 
 * @author Florian
 */
public class Terminal {
	private Scanner scanner;

	/**
	 * default constructor of the object just initializes some internal stuff
	 */
	public Terminal() {
		this.scanner = new Scanner(System.in);
	}

	/**
	 * reads a string from the stdin
	 * 
	 * @return the read string
	 */
	public String read() {
		return this.scanner.nextLine();
	}

	/**
	 * writes a string on the stdout
	 * 
	 * @param line
	 *            the string to write out
	 */
	public void write(String line) {
		System.out.println(line);
	}
}
