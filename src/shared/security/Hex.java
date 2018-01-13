package shared.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Hex {
	private Random rand;
	private char[] charTable;
	public static final String charList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890-!+";

	public Hex(String init) {
		this.setUpRandom(init);
		this.setUpCharTable();
	}

	private void setUpRandom(String init) {
		IntWrapper randInit = new IntWrapper();
		init.chars().forEach((chr) -> {
			int chrSum = 1;
			for(int i = 0;i <= chr;i++) {
				chrSum += (i+1) * chr * chr;
			}
			randInit.add(chrSum);
		});

		this.rand = new Random(randInit.get());
	}

	private void setUpCharTable() {
		this.charTable = new char[16];

		ArrayList<Character> chars = new ArrayList<Character>();
		Hex.charList.chars().forEach((chr) -> { chars.add((char) chr); });
		Collections.shuffle(chars,this.rand);

		for(int i = 0;i < this.charTable.length;i++) {
			this.charTable[i] = chars.get(i);
		}
	}

	public String toHex(byte[] input) {
		StringBuilder out = new StringBuilder("");

		byte[] bytes = input;
		byte upper;
		byte lower;
		byte mask = 15;
		byte shift = 4;

		for(int i = 0;i < bytes.length;i++) {
			lower = (byte) (bytes[i] & mask);
			upper = (byte) ((bytes[i] >> shift) & mask);

			out.append(this.charTable[upper]);
			out.append(this.charTable[lower]);
		}

		return out.toString();
	}

	public byte[] fromHex(String input) {
	    	char[] chars = input.toCharArray();
		byte[] bytes = new byte[chars.length/2];
		byte resByte = 0;
		byte shift = 4;
		int byteCount = 0;
		boolean left = true;

		for(int i = 0;i < chars.length;i++) {
			for(int j = 0;j < this.charTable.length;j++) {
				if(this.charTable[j] == chars[i]) {
					if(left) {
						resByte = (byte) (j << shift);
					} else {
						resByte = (byte) (resByte | j);
						bytes[byteCount] = resByte;
						byteCount++;
					}

					break;
				}
			}
			left = !left;
		}

		return bytes;
	}
}
