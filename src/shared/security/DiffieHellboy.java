package shared.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class DiffieHellboy {
    private KeyPair keyPair;
    private KeyAgreement keyAgree;

    public void createKeyPair() throws Exception {
	KeyPairGenerator keyGen = null;

	keyGen = KeyPairGenerator.getInstance("DH");

	keyGen.initialize(2048);
	this.keyPair = keyGen.generateKeyPair();
	this.keyAgree = KeyAgreement.getInstance("DH");
	this.keyAgree.init(this.keyPair.getPrivate());
    }

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

    public void addPKToKeyAgreement(byte[] encodedKey) throws Exception {
	KeyFactory kF;
	kF = KeyFactory.getInstance("DH");

	X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(encodedKey);
	PublicKey pK = kF.generatePublic(x509KeySpec);

	this.keyAgree.doPhase(pK, true);
    }

    public byte[] getEncodedPublicKey() {
	return this.keyPair.getPublic().getEncoded();
    }

    public byte[] generateSecret() {
	return this.keyAgree.generateSecret();
    }
}
