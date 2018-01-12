package shared.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class DiffieHellboy {
    private KeyPair keyPair;

    public void createKeyPair() {
	KeyPairGenerator keyGen = null;

	try {
	    keyGen = KeyPairGenerator.getInstance("DH");
	} catch (NoSuchAlgorithmException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    System.exit(0);
	}

	keyGen.initialize(2048);
	this.keyPair = keyGen.generateKeyPair();
    }

    public byte[] getEncodedPublicKey() {
	return this.keyPair.getPublic().getEncoded();
    }
}
