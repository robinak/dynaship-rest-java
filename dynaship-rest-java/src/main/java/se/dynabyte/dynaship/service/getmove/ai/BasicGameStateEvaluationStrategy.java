package se.dynabyte.dynaship.service.getmove.ai;

import java.util.Random;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;

public class BasicGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Random random = new Random();

	@Override
	public Coordinates getMove(GameState gameState) {
		int boardSize = gameState.getBoardSize();
		int x = getRandomNumber(boardSize);
		int y = getRandomNumber(boardSize);
		return new Coordinates(x, y);
	}
	
	private int getRandomNumber(int upperLimitExclusive) {
		return random.nextInt(upperLimitExclusive);
	}

}
