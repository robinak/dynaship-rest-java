package se.dynabyte.dynaship.service.getmove.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.util.advanced.ShotUtil;

public class SimpleGameStateEvaluationStrategy implements GameStateEvaluationStrategy {

	private static final Random random = new Random();
	
	/**
	 *  Get a random coordinate that has not already been fired upon.
	 */
	@Override
	public Coordinates getMove(GameState gameState) {
		
		List<Coordinates> candidates = getAllCoordinates(gameState.getBoardSize()); 
		Collection<Coordinates> existingShotCoordinates = ShotUtil.getCoordinates(gameState.getShots());
		candidates.removeAll(existingShotCoordinates);
		
		int randomIndex = getRandomNumber(candidates.size());
		return candidates.get(randomIndex);
	}
	
	private List<Coordinates> getAllCoordinates(int boardSize) {
		List<Coordinates> allCoordinates = new ArrayList<Coordinates>();
		
		for(int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				allCoordinates.add(new Coordinates(j, i));
			}
		}
		return allCoordinates;
	}
	
	private int getRandomNumber(int upperLimitExclusive) {
		return random.nextInt(upperLimitExclusive);
	}

}
