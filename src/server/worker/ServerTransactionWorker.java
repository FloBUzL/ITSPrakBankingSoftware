package server.worker;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

import server.connection.ServerConnectionData;
import shared.connection.Message;
import shared.constants.Misc;
import shared.exception.AuthenticationException;
import shared.exception.GeneralException;
import shared.security.RandomString;
import shared.superclassifragilistic.Worker;

/**
 * worker for doing a transaction
 * @author Florian
 */
public class ServerTransactionWorker extends ServerWorker {
    /**
     * constructor
     * @param connectionData the connection's data
     */
    public ServerTransactionWorker(ServerConnectionData connectionData) {
	super(connectionData);
    }

    @Override
    /**
     * doesn't do anything yet
     */
    public Worker setup() {
	return this;
    }

    @Override
    /**
     * runs the auth process and transaction
     */
    public Worker run() throws Exception {
	// authenticate user's device
	this.checkAuthentication();
	if(!this.connectionData.isAuthenticated()) {
	    this.connectionData.debug("not authenticated");
	    return this;
	}
	this.connectionData.debug("authenticated");
	// do transaction
	this.handleTransaction();
	this.succeeded = true;

	return this;
    }

    private void checkAuthentication() throws Exception {
	// check if device is already authenticated
	if(this.connectionData.isAuthenticated()) {
	    this.connectionData.getConnection().write(new Message()
		.addData("task", "transaction")
		.addData("message", "pass_authentication"));
	    return;
	}

	String nonce = new RandomString(32).toString();
	Message askForAuthentication = new Message()
		.addData("task", "transaction")
		.addData("message", "request_authentication")
		.addData("nonce", nonce);
	Message finishRegistration = new Message()
		.addData("task", "transaction")
		.addData("message", "finish_registration");
	Message authCodeAnswer = new Message()
		.addData("task", "transaction");
	boolean loop = true;
	Message clientRequest;
	Message crCode;
	String cr;

	this.connectionData.getConnection().write(askForAuthentication);

	while(loop) {
	    clientRequest = this.connectionData.getConnection().read();
	    switch(clientRequest.getData("message")) {
	    	case "authenticate_device" :
	    	    String clientCR = clientRequest.getData("cr");
	    	    String deviceCode = clientRequest.getData("device");
	    	    Message authReply = new Message()
	    		    .addData("task", "transaction");

	    	    // check if user authenticated successfully
	    	    if(this.connectionData.getDatabase().checkAuthCR(this.connectionData.getUserName(), deviceCode, nonce, clientCR)) {
	    		loop = false;
	    		this.connectionData.authenticate();
	    		authReply.addData("message", "success");
	    	    } else {
	    		authReply.addData("message", "failed");
	    	    }
	    	    this.connectionData.getConnection().write(authReply);
	    	break;
	    	case "register_device" :
	    	    // registers new device
	    	    String clientCode = clientRequest.getData("code");
	    	    if(clientCode.length() != 16) {
	    		throw new AuthenticationException();
	    	    }
	    	    String serverCodeFirstPart = new RandomString(8).toString();
	    	    String serverCodeSecondPart = new RandomString(8).toString();

	    	    // sends auth code
	    	    this.connectionData.debug("new device registered - authcode: " + serverCodeFirstPart);
	    	    this.sendMail(this.connectionData.getDatabase().getUserMail(this.connectionData.getUserName()), serverCodeFirstPart);

	    	    this.connectionData.getDatabase().registerDevice(this.connectionData.getUserName(),clientCode + serverCodeFirstPart + serverCodeSecondPart);

	    	    this.connectionData.getConnection().write(finishRegistration);
	    	    crCode = this.connectionData.getConnection().read();
	    	    cr = crCode.getData("cr");
	    	    // checks auth code
	    	    if(this.connectionData.getDatabase().checkAuthCR(this.connectionData.getUserName(),clientCode + serverCodeFirstPart + serverCodeSecondPart,nonce,cr)) {
	    		this.connectionData.authenticate();
	    		authCodeAnswer.addData("message", "success");
	    		authCodeAnswer.addData("code", serverCodeFirstPart + serverCodeSecondPart);
	    	    } else {
	    		authCodeAnswer.addData("message", "failed");
	    		this.connectionData.getDatabase().deleteDevice(this.connectionData.getUserName(),clientCode + serverCodeFirstPart + serverCodeSecondPart);
	    		this.connectionData.getAuthErrors().increase(this.connectionData.getConnection().getIP(), this.connectionData.getUserName());
	    	    }
	    	    this.connectionData.getConnection().write(authCodeAnswer);
	    	    loop = false;
	    	break;
	    	default :
	    	    throw new AuthenticationException();
	    }
	}
    }

    private void handleTransaction() throws Exception {
	Message transaction;
	String receiver;
	String amount;
	Message transactionResponse = new Message()
		.addData("task", "transaction");

	transaction = this.connectionData.getConnection().read();
	receiver = this.connectionData.getAes().decode(transaction.getData("receiver"));
	amount = this.connectionData.getAes().decode(transaction.getData("amount"));
	if(!amount.matches("[0-9]|10")) {
	    throw new GeneralException();
	}

	this.connectionData.debug("executing transaction...");

	if(this.connectionData.getDatabase().doTransaction(this.connectionData.getUserName(),receiver,Integer.parseInt(amount))) {
	    transactionResponse.addData("message", "success");
	} else {
	    transactionResponse.addData("message", "error");
	}

	this.connectionData.getConnection().write(transactionResponse);
    }

    private void sendMail(String email,String authcode) {
	if(!Misc.SEND_MAIL) {
	    return;
	}
	try {
	    Properties props = System.getProperties();
	    props.put("mail.smtps.host", "localhost");
	    props.put("mail.smtps.auth", "true");
	    Session session = Session.getInstance(props, null);

	    // Create message
	    javax.mail.Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress("test@its-bank"));
	    msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(email, false));
	    msg.setSubject("Confirmation code");
	    msg.setText("Your confirmation code: " + authcode);
	    msg.setHeader("X-Mailer", "ITS-BankServer");
	    msg.setSentDate(new Date());

	    // Send message
	    SMTPTransport t = (SMTPTransport)session.getTransport("smtp");
	    t.connect("localhost", "test@its-bank", "test");
	    t.sendMessage(msg, msg.getAllRecipients());
	    t.close();
	} catch(Exception e) {

	}

    }
}
