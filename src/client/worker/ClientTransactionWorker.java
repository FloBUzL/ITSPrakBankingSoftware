package client.worker;

import client.connection.ClientConnectionData;
import shared.connection.Message;
import shared.constants.Misc;
import shared.security.Hash;
import shared.security.RandomString;
import shared.superclassifragilistic.Worker;

public class ClientTransactionWorker extends ClientWorker {

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
	String nonce;
	String serverDeviceCode;

	String authCode;
	String email;
	String hash;

	this.connectionData.getConnection().write(requestTransaction);
	nonceMsg = this.connectionData.getConnection().read();
	nonce = nonceMsg.getData("nonce");

	if(Misc.ALLOW_PERMANENT_DEVICES && this.isDeviceRegistered()) {
	    // TODO: authenticate device
	    // TODO: return
	}

	this.connectionData.getConnection().write(registerDevice);

	deviceCode = this.connectionData.getConnection().read();
	serverDeviceCode = deviceCode.getData("code");
	if(Misc.ALLOW_PERMANENT_DEVICES) {
	    // TODO: make device file
	}

	this.connectionData.getTerminal().write("enter authentication code");
	authCode = this.connectionData.getTerminal().read();
	this.connectionData.getTerminal().write("enter email adress");
	email = this.connectionData.getTerminal().read();

	hash = new Hash(authCode + email + nonce).toString();
	sendAuthCode.addData("cr", hash);
    }

    private boolean isDeviceRegistered() {
	// TODO: check for registered device
	return false;
    }
}
