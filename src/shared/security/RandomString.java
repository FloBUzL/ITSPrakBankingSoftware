package shared.security;

import java.util.Random;

public class RandomString {
    private String randString = null;

    public RandomString(int stringLength) {
	Random rand = new Random();
	char[] chars = Hex.charList.toCharArray();
	StringBuilder sb = new StringBuilder();

	for(int i = 0;i < stringLength;i++) {
	    sb.append(chars[rand.nextInt(chars.length)]);
	}

	this.randString = sb.toString();
    }

    public String toString() {
	return this.randString;
    }

}
