package se.dynabyte.dynaship.service.getmove.util.advanced;

import java.util.Random;

public class Randomizer {
	
	private static final Random random = new Random();
	
	/**
	 * @param upperLimitExclusive - the bound on the random number to be returned. Must be positive.
	 * @return random {@code int} value between 0 (inclusive) and the specified upper limit (exclusive).
	 */
	public int getRandomInt(int upperLimitExclusive) {
		return random.nextInt(upperLimitExclusive);
	}

}
