package se.dynabyte.dynaship.service.getmove.util.advanced;

import java.util.Random;

public class RandomUtil {
	
	private static final Random random = new Random();
	
	public int getRandomNumber(int upperLimitExclusive) {
		return random.nextInt(upperLimitExclusive);
	}

}
