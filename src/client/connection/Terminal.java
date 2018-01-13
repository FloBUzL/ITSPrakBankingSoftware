package client.connection;

import java.util.Scanner;

public class Terminal {
    private Scanner scanner;

    public Terminal() {
	this.scanner = new Scanner(System.in);
    }

    public void write(String line) {
	System.out.println(line);
    }

    public String read() {
	return this.scanner.nextLine();
    }
}
