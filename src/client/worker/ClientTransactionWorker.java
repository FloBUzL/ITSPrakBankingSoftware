package client.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import client.connection.ClientConnectionData;
import shared.connection.Message;
import shared.constants.Misc;
import shared.security.Hash;
import shared.security.RandomString;
import shared.superclassifragilistic.Worker;

/**
 * the worker used for authenticate the device and run a transaction
 * 
 * @author Florian
 */
public class ClientTransactionWorker extends ClientWorker {
	private boolean authenticated = false;

	/**
	 * the constructor
	 * 
	 * @param connectionData
	 *            the connection's data
	 */
	public ClientTransactionWorker(ClientConnectionData connectionData) {
		super(connectionData);
	}

	/**
	 * authenticates the device if already registered
	 * 
	 * @param nonce
	 *            the current nonce
	 * @return true if succeeded
	 */
	private boolean autoAuthenticateDevice(String nonce) {
		String devCode = "";
		String authCode = "";
		String cr = "";
		Message autoAuthenticate = new Message().addData("task", "transaction").addData("message",
				"authenticate_device");
		Message reply;

		try {
			// checks if file is existant and reads the device code
			File device = new File("resources/device_" + this.connectionData.getUsername());
			if (!device.exists()) {
				return false;
			}
			BufferedReader br = new BufferedReader(new FileReader(device));
			devCode = br.readLine();
			br.close();
			// generated authcode
			authCode = devCode.split(";")[1];
			devCode = devCode.split(";")[0];

			devCode = this.connectionData.getAes().encode(devCode);

			// generate chalange-response

			cr = new Hash(authCode + nonce).toString();
			autoAuthenticate.addData("device", devCode);
			autoAuthenticate.addData("cr", cr);
			this.connectionData.getConnection().write(autoAuthenticate);
			reply = this.connectionData.getConnection().read();
			if (reply.getData("message").equals("failed")) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private void checkForAuthentification() throws Exception {
		String clientDeviceCode = new RandomString(16).toString();
		Message requestTransaction = new Message().addData("task", "transaction").addData("message", "request");
		Message registerDevice = new Message().addData("task", "transaction").addData("message", "register_device")
				.addData("code", clientDeviceCode);
		Message sendAuthCode = new Message().addData("task", "transaction").addData("message", "send_authcode");
		Message nonceMsg;
		Message authReply;

		String nonce;
		String serverDeviceCode;
		String authCode;
		String hash;

		// receive a nonce
		this.connectionData.getConnection().write(requestTransaction);
		nonceMsg = this.connectionData.getConnection().read();

		// if the device is already authenticated, skip
		if (nonceMsg.getData("message").equals("pass_authentication")) {
			this.authenticated = true;
			return;
		}

		nonce = nonceMsg.getData("nonce");

		// if the device is already registered, authenticate
		if (Misc.ALLOW_PERMANENT_DEVICES) {
			this.authenticated = this.autoAuthenticateDevice(nonce);
			if (this.authenticated == true) {
				return;
			}
		}

		this.connectionData.getConnection().write(registerDevice);

		// wait for the server's message
		this.connectionData.getConnection().read();

		// authenticate the device
		this.connectionData.getTerminal().write("enter authentication code");
		authCode = this.connectionData.getTerminal().read();

		hash = new Hash(authCode + nonce).toString();
		sendAuthCode.addData("cr", hash);

		this.connectionData.getConnection().write(sendAuthCode);

		authReply = this.connectionData.getConnection().read();
		// check if authentication worked
		if (authReply.getData("message").equals("success")) {
			serverDeviceCode = authReply.getData("code");
			// writes device file
			if (Misc.ALLOW_PERMANENT_DEVICES) {
				this.writeDeviceFile(clientDeviceCode + serverDeviceCode,authCode);
			}
			this.connectionData.getTerminal().write("device registered");
			this.authenticated = true;
		} else {
			this.connectionData.getTerminal().write("device registration failed");
		}
	}

	/**
	 * handles the money transaction to another user
	 * 
	 * @throws Exception
	 */
	private void handleTransaction() throws Exception {
		String receiver;
		String amount;
		Message sendTransaction = new Message().addData("task", "transaction").addData("message", "do_transaction");
		Message response;

		this.connectionData.getTerminal().write("enter receiver");
		receiver = this.connectionData.getTerminal().read();

		// tries until a amount is typed that's between 1 and 10 (inclusive)
		do {
			this.connectionData.getTerminal().write("enter amount (1-10)");
			amount = this.connectionData.getTerminal().read();
		} while (!amount.matches("[0-9]|10"));

		// does the transaction
		sendTransaction.addData("receiver", this.connectionData.getAes().encode(receiver)).addData("amount",
				this.connectionData.getAes().encode(amount));

		this.connectionData.getConnection().write(sendTransaction);

		response = this.connectionData.getConnection().read();
		if (response.getData("message").equals("success")) {
			this.connectionData.getTerminal().write("transaction done");
		} else {
			this.connectionData.getTerminal().write("transaction failed. entered correct username?");
		}
	}

	@Override
	/**
	 * runs authentication and transaction
	 */
	public Worker run() throws Exception {
		// first authenticate the device
		this.checkForAuthentification();
		if (!this.authenticated) {
			return this;
		}
		this.handleTransaction();

		return this;
	}

	@Override
	/**
	 * doesn't do anything yet
	 */
	public Worker setup() {
		return this;
	}

	private void writeDeviceFile(String deviceCode,String authCode) {
		try {
			PrintWriter file = new PrintWriter("resources/device_" + this.connectionData.getUsername());
			file.write(deviceCode + ";" + authCode);
			file.close();
		} catch (Exception e) {

		}
	}
}
