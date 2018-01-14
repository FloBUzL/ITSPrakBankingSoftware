package shared.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 * some methods for doing a dhe
 * @author Florian
 */
public class DiffieHellboy {
    private KeyPair keyPair;
    private KeyAgreement keyAgree;

    /**
     * creates a key pair
     * @throws Exception
     */
    public void createKeyPair() throws Exception {
	KeyPairGenerator keyGen = null;

	keyGen = KeyPairGenerator.getInstance("DH");

	keyGen.initialize(2048);
	this.keyPair = keyGen.generateKeyPair();
	this.keyAgree = KeyAgreement.getInstance("DH");
	this.keyAgree.init(this.keyPair.getPrivate());
    }

    /**
     * creates a key pair for a given public key
     * @param encodedKey the public key to use for key creation
     * @throws Exception
     */
    public void createKeyPair(byte[] encodedKey) throws Exception {
	KeyFactory kF;
	kF = KeyFactory.getInstance("DH");

	X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(encodedKey);
	PublicKey pK = kF.generatePublic(x509KeySpec);

	DHParameterSpec dhParamFromPK = ((DHPublicKey) pK).getParams();
	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
	keyGen.initialize(dhParamFromPK);
	this.keyPair = keyGen.generateKeyPair();
	this.keyAgree = KeyAgreement.getInstance("DH");
	this.keyAgree.init(this.keyPair.getPrivate());
    }

    /**
     * add a public key to an agreement
     * @param encodedKey the key to use
     * @throws Exception
     */
    public void addPKToKeyAgreement(byte[] encodedKey) throws Exception {
	KeyFactory kF;
	kF = KeyFactory.getInstance("DH");

	X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(encodedKey);
	PublicKey pK = kF.generatePublic(x509KeySpec);

	this.keyAgree.doPhase(pK, true);
    }

    /**
     * get the own public key
     * @return the public key
     */
    public byte[] getEncodedPublicKey() {
	return this.keyPair.getPublic().getEncoded();
    }

    /**
     * generated a shared secret
     * @return the generated secret
     */
    public byte[] generateSecret() {
	return this.keyAgree.generateSecret();
    }
}
