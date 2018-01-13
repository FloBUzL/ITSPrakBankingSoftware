package shared.security;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private SecretKeySpec aesKey;
    private byte[] sharedSecret;
    private Hex hex;

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

    public String encode(String raw) throws Exception {
	Cipher encCipher = this.getCipher(false);

	return this.hex.toHex(encCipher.doFinal(raw.getBytes()));
    }

    public String decode(String encoded) throws Exception {
	Cipher decCipher = this.getCipher(true);

	return new String(decCipher.doFinal(this.hex.fromHex(encoded)));
    }
}
