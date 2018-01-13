package client.worker;

import client.connection.ClientConnectionData;
import shared.connection.Message;
import shared.constants.Misc;
import shared.security.Hash;
import shared.security.RandomString;
import shared.superclassifragilistic.Worker;

public class ClientTransactionWorker extends ClientWorker {
    private boolean authenticated = false;

    public ClientTransactionWorker(ClientConnectionData connectionData) {
	super(connectionData);
	// TODO Auto-generated constructor stub
    }

    @Override
    public Worker setup() {
	// TODO Auto-generated method stub
	return this;
    }

    @Override
    public Worker run() throws Exception {
	this.checkForAuthentification();
	if(!this.authenticated) {
	    return this;
	}
	this.handleTransaction();

	return this;
    }

    private void checkForAuthentification() throws Exception {
	String clientDeviceCode = new RandomString(16).toString();
	Message requestTransaction = new Message()
		.addData("task", "transaction")
		.addData("message", "request");
	Message registerDevice = new Message()
		.addData("task", "transaction")
		.addData("message", "register_device")
		.addData("code", clientDeviceCode);
	Message sendAuthCode = new Message()
		.addData("task", "transaction")
		.addData("message", "send_authcode");
	Message nonceMsg;
	Message deviceCode;
	Message authReply;
	String nonce;
	String serverDeviceCode;

	String authCode;
	String email;
	String hash;

	this.connectionData.getConnection().write(requestTransaction);
	nonceMsg = this.connectionData.getConnection().read();

	if(nonceMsg.getData("message").equals("pass_authentication")) {
	    this.authenticated = true;
	    return;
	}

	nonce = nonceMsg.getData("nonce");

	if(Misc.ALLOW_PERMANENT_DEVICES && this.isDeviceRegistered()) {
	    // TODO: authenticate device
	    // TODO: return
	}

	this.connectionData.getConnection().write(registerDevice);

	deviceCode = this.connectionData.getConnection().read();
	serverDeviceCode = deviceCode.getData("code");

	this.connectionData.getTerminal().write("enter authentication code");
	authCode = this.connectionData.getTerminal().read();
	this.connectionData.getTerminal().write("enter email adress");
	email = this.connectionData.getTerminal().read();

	hash = new Hash(authCode + email + nonce).toString();
	sendAuthCode.addData("cr", hash);

	this.connectionData.getConnection().write(sendAuthCode);

	authReply = this.connectionData.getConnection().read();
	if(authReply.getData("message").equals("success")) {
	    if(Misc.ALLOW_PERMANENT_DEVICES) {
		    // TODO: make device file
	    }
	    this.connectionData.getTerminal().write("device registered");
	    this.authenticated = true;
	} else {
	    this.connectionData.getTerminal().write("device registration failed");
	}
    }

    private void handleTransaction() throws Exception {
	String receiver;
	String amount;
	Message sendTransaction = new Message()
		.addData("task", "transaction")
		.addData("message", "do_transaction");
	Message response;

	this.connectionData.getTerminal().write("enter receiver");
	receiver = this.connectionData.getTerminal().read();

	do {
	    this.connectionData.getTerminal().write("enter amount (1-10)");
	    amount = this.connectionData.getTerminal().read();
	} while(!amount.matches("[0-9]|10"));

	sendTransaction
		.addData("receiver", this.connectionData.getAes().encode(receiver))
		.addData("amount", this.connectionData.getAes().encode(amount));

	this.connectionData.getConnection().write(sendTransaction);

	response = this.connectionData.getConnection().read();
	if(response.getData("message").equals("success")) {
	    this.connectionData.getTerminal().write("transaction done");
	} else {
	    this.connectionData.getTerminal().write("transaction failed. entered correct username?");
	}
    }

    private boolean isDeviceRegistered() {
	// TODO: check for registered device
	return false;
    }
}
