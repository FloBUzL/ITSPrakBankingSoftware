package shared.security;

import java.util.Random;

/**
 * creates a random string
 * @author Florian
 */
public class RandomString {
    private String randString = null;

    /**
     * creates a random string with the given length
     * @param stringLength the given length
     */
    public RandomString(int stringLength) {
	Random rand = new Random();
	char[] chars = Hex.charList.toCharArray();
	StringBuilder sb = new StringBuilder();

	for(int i = 0;i < stringLength;i++) {
	    sb.append(chars[rand.nextInt(chars.length)]);
	}

	this.randString = sb.toString();
    }

    /**
     * getter for the string
     */
    public String toString() {
	return this.randString;
    }

}
