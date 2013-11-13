package se.dynabyte.dynaship.service.getmove.ai.advanced;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.ShotCollector;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.model.State;
import se.dynabyte.dynaship.service.getmove.util.advanced.CoordinatesUtil;
import se.dynabyte.dynaship.service.getmove.util.advanced.Randomizer;
import se.dynabyte.dynaship.service.getmove.util.advanced.ShipsUtil;

public class SimpleGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleGameStateEvaluationStrategy.class);

	private final ShipsUtil shipsUtil;
	private final CoordinatesUtil coordinatesUtil;
	private final Randomizer randomUtil;
	
	public SimpleGameStateEvaluationStrategy(ShipsUtil shipsUtil, CoordinatesUtil coordinatesUtil, Randomizer randomUtil) {
		this.shipsUtil = shipsUtil;
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
		
		Collection<Shot> shots = gameState.getShots();
		Collection<Shot> hitsOnSeaworthyShips = new ShotCollector(shots).collect(State.SEAWORTHY);
		Collection<Coordinates> seaworthyCoordinates = coordinatesUtil.getCoordinates(hitsOnSeaworthyShips);
		
		int minAliveShipLenght = shipsUtil.getMinimumLenghtOfAliveShip(gameState.getShips());
		int maxAliveShipLenght = shipsUtil.getMaximumLengthOfAliveShip(gameState.getShips());
		
		while (!candidates.isEmpty()) {
			int randomIndex = randomUtil.getRandomInt(candidates.size());
			Coordinates candidate = candidates.get(randomIndex);
			
			
			if (coordinatesUtil.hasEnoughUnexploredOrSeaworthyNeighboursToFitSmallestSeaworthyShip(candidate, minAliveShipLenght, maxAliveShipLenght, candidates, seaworthyCoordinates)) {
				return candidate;
			}
			
			candidates.remove(candidate);
			log.debug("Removed candidate: {} since smallest ship cannot fit around these coordinates.", candidate);
		}
		
		return null;
	}
}
