package se.dynabyte.dynaship.service.getmove.ai.advanced;

import java.util.Collection;
import java.util.List;

import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.util.advanced.CoordinatesUtil;
import se.dynabyte.dynaship.service.getmove.util.advanced.RandomUtil;

public class SimpleGameStateEvaluationStrategy implements GameStateEvaluationStrategy {

	private final CoordinatesUtil coordinatesUtil;
	private final RandomUtil randomUtil;
	
	public SimpleGameStateEvaluationStrategy(CoordinatesUtil coordinatesUtil, RandomUtil randomUtil) {
		this.coordinatesUtil = coordinatesUtil;
		this.randomUtil = randomUtil;
	}
	
	/**
	 *  Get a random coordinate that has not already been fired upon, or null if no coordinate could be found.
	 */
	@Override
	public Coordinates getMove(GameState gameState) {
		
		List<Coordinates> candidates = coordinatesUtil.getAllCoordinates(gameState.getBoardSize()); 
		Collection<Coordinates> existingShotCoordinates = coordinatesUtil.getCoordinates(gameState.getShots());
		candidates.removeAll(existingShotCoordinates);
		
		if (!candidates.isEmpty()) {
			int randomIndex = randomUtil.getRandomNumber(candidates.size());
			return candidates.get(randomIndex);
		}
		
		return null;
	}
	
}
