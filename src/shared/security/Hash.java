package shared.security;

import java.security.MessageDigest;

/**
 * hashes a string
 * 
 * @author Florian
 */
public class Hash {
	private String hash;
	private Hex hex;

	/**
	 * hashes a string
	 * 
	 * @param toHash
	 *            the string to hash
	 * @throws Exception
	 */
	public Hash(String toHash) throws Exception {
		this.hex = new Hex("klaus!klaus!klausisttoll!staplerfahrerklaus");
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		this.hash = this.hex.toHex(digest.digest(toHash.getBytes()));
	}

	/**
	 * the hashed string
	 */
	public String toString() {
		return this.hash;
	}
}
