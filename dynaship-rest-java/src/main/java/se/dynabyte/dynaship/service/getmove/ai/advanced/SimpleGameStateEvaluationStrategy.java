package se.dynabyte.dynaship.service.getmove.ai.advanced;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.util.advanced.CoordinatesUtil;

public class SimpleGameStateEvaluationStrategy implements GameStateEvaluationStrategy {

	private static final Random random = new Random();
	
	/**
	 *  Get a random coordinate that has not already been fired upon, or null if no coordinate could be found.
	 */
	@Override
	public Coordinates getMove(GameState gameState) {
		
		List<Coordinates> candidates = CoordinatesUtil.getAllCoordinates(gameState.getBoardSize()); 
		Collection<Coordinates> existingShotCoordinates = CoordinatesUtil.getCoordinates(gameState.getShots());
		candidates.removeAll(existingShotCoordinates);
		
		if (!candidates.isEmpty()) {
			int randomIndex = getRandomNumber(candidates.size());
			return candidates.get(randomIndex);
		}
		
		return null;
	}
	
	private int getRandomNumber(int upperLimitExclusive) {
		return random.nextInt(upperLimitExclusive);
	}

}
