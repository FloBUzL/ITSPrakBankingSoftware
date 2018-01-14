package shared.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * aes object for encrypting and decrypting data
 * @author Florian
 */
public class AES {
    private SecretKeySpec aesKey;
    private byte[] sharedSecret;
    private Hex hex;

    /**
     * initializes the object
     * @param sharedSecret the key to use
     * @throws Exception
     */
    public AES(byte[] sharedSecret) throws Exception {
	this.aesKey = new SecretKeySpec(sharedSecret, 0, 16, "AES");
	this.sharedSecret = sharedSecret;
	this.hex = new Hex("heythere!justhangingaround");
    }

    private Cipher getCipher(boolean decode) throws Exception {
	Cipher newCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	int opMode = decode ? Cipher.DECRYPT_MODE : Cipher.ENCRYPT_MODE;

	newCipher.init(opMode, this.aesKey, new IvParameterSpec(this.sharedSecret,0,16));

	return newCipher;
    }

    /**
     * encodes the given string
     * @param raw string to encode
     * @return the encoded string
     * @throws Exception
     */
    public String encode(String raw) throws Exception {
	Cipher encCipher = this.getCipher(false);

	return this.hex.toHex(encCipher.doFinal(raw.getBytes()));
    }

    /**
     * decodes the given string
     * @param encoded string to decode
     * @return the decoded string
     * @throws Exception
     */
    public String decode(String encoded) throws Exception {
	Cipher decCipher = this.getCipher(true);

	return new String(decCipher.doFinal(this.hex.fromHex(encoded)));
    }
}
