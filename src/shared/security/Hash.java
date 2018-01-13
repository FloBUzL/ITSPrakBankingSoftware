package shared.security;

import java.security.MessageDigest;

public class Hash {
    private String hash;
    private Hex hex;

    public Hash(String toHash) throws Exception {
	this.hex = new Hex("klaus!klaus!klausisttoll!staplerfahrerklaus");
	MessageDigest digest = MessageDigest.getInstance("SHA-256");
	this.hash = this.hex.toHex(digest.digest(toHash.getBytes()));
    }

    public String toString() {
	return this.hash;
    }
}
