package server.worker;

import server.connection.ServerConnectionData;
import shared.connection.Message;
import shared.exception.AuthenticationException;
import shared.security.AES;
import shared.security.DiffieHellboy;
import shared.security.Hex;
import shared.security.RandomString;
import shared.superclassifragilistic.Worker;

/**
 * handles the authentication process
 * 
 * @author Florian
 */
public class ServerLoginWorker extends ServerWorker {
	private DiffieHellboy dh;
	private Hex hex;

	/**
	 * constructor
	 * 
	 * @param connectionData
	 *            the connection's data
	 */
	public ServerLoginWorker(ServerConnectionData connectionData) {
		super(connectionData);
	}

	@Override
	/**
	 * runs the auth process
	 */
	public Worker run() throws Exception {
		Message publicKeyMessage = new Message();
		Message publicClientKey;
		Message nonceMessage = new Message();
		Message loginMessage;
		Message loginResponse = new Message();

		byte[] pClientKey;
		byte[] sharedSecret;

		String key;
		String nonce;
		String username;
		String clientCR;

		// creates keys and sends public key
		this.dh.createKeyPair();
		key = hex.toHex(this.dh.getEncodedPublicKey());

		publicKeyMessage.addData("task", "key_exchange").addData("key", key);

		this.connectionData.getConnection().write(publicKeyMessage);

		// gets client's public key
		publicClientKey = this.connectionData.getConnection().read();
		pClientKey = hex.fromHex(publicClientKey.getData("key"));
		this.dh.addPKToKeyAgreement(pClientKey);

		// generates aes object
		sharedSecret = this.dh.generateSecret();
		this.connectionData.setAes(new AES(sharedSecret));

		// sends nonce to client
		nonce = new RandomString(32).toString();

		nonceMessage.addData("task", "send_nonce").addData("nonce", this.connectionData.getAes().encode(nonce));

		this.connectionData.getConnection().write(nonceMessage);

		// checks if user authenticated successfully
		loginMessage = this.connectionData.getConnection().read();
		clientCR = this.connectionData.getAes().decode(loginMessage.getData("cr"));
		username = loginMessage.getData("user");

		if (this.connectionData.getAuthErrors().isUserBlocked(username)
				|| this.connectionData.getAuthErrors().isHostBlocked(this.connectionData.getConnection().getIP())) {
			throw new AuthenticationException();
		}

		if (this.connectionData.getDatabase().checkCR(username, nonce, clientCR)) {
			loginResponse.addData("task", "login").addData("message", "Login successful");
			this.succeeded = true;
			this.connectionData.setUserName(username);
		} else {
			loginResponse.addData("task", "login").addData("message", "Login failed");
			this.connectionData.getAuthErrors().increase(this.connectionData.getConnection().getIP(), username);
		}

		this.connectionData.getConnection().write(loginResponse);

		return this;
	}

	@Override
	/**
	 * sets up the objects needed for the key exchange
	 */
	public Worker setup() {
		this.hex = new Hex("sorrybutthatsnosecret");
		this.dh = new DiffieHellboy();
		return this;
	}
}
