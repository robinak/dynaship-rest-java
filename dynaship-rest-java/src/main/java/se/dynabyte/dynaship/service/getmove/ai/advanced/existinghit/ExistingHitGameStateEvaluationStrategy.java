package se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Ship;
import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.model.State;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroups;
import se.dynabyte.dynaship.service.getmove.util.advanced.CoordinatesUtil;
import se.dynabyte.dynaship.service.getmove.util.advanced.Randomizer;
import se.dynabyte.dynaship.service.getmove.util.advanced.ShipsUtil;

/**
 * This strategy focus on finding existing hits on seaworthy ships
 *
 */
public class ExistingHitGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(ExistingHitGameStateEvaluationStrategy.class);
	
	private final ShipsUtil shipsUtil;
	private final CoordinatesUtil coordinatesUtil;
	private final Randomizer randomUtil;
	
	public ExistingHitGameStateEvaluationStrategy(ShipsUtil shipsUtil, CoordinatesUtil coordinatesUtil, Randomizer randomUtil) {
		this.shipsUtil = shipsUtil;
		this.coordinatesUtil = coordinatesUtil;
		this.randomUtil = randomUtil;
	}

	/**
	 * @return the coordinates of the next shot or {@code null} if no valid
	 *         target could be acquired near a seaworthy ship.
	 */
	@Override
	public Coordinates getMove(GameState gameState) {
		
		Collection<Shot> shots = gameState.getShots();
		Collection<Shot> hitsOnSeaworthyShips = new ShotCollector(shots).collect(State.SEAWORTHY);
		
		CoordinatesGroups groups = new CoordinatesGroups();
		
		for (Shot shot : hitsOnSeaworthyShips) {
			groups.add(shot.getCoordinates());
		}
		
		groups.mergeAdjacent();
		
		log.debug("Resulting groups: {}", groups);
		
		Coordinates target = getTarget(groups, shots, hitsOnSeaworthyShips, gameState.getShips(), gameState.getBoardSize());
		return target;
	}
	
	private Coordinates getTarget(CoordinatesGroups groups, Collection<Shot> shots, Collection<Shot> hitsOnSeaworthyShips, Collection<Ship> ships, int boardSize) {
		CoordinatesGroups groupsLargestFirst = new CoordinatesGroups(groups);
		groupsLargestFirst.sortBySizeInDecendingOrder();
		
		for (CoordinatesGroup group : groupsLargestFirst) {
			List<Coordinates> targetCandidates;
			
			if (group.size() == 1) {
				targetCandidates = coordinatesUtil.getNonDiagonalNeighbours(group.first());
				
			} else {
				targetCandidates = coordinatesUtil.getNeighboursInGoupDirection(group);
			}
			
			List<Coordinates> existingShotCoordinates = coordinatesUtil.getCoordinates(shots);
			log.debug("Removing existing shot coordinates: {}", existingShotCoordinates);
			targetCandidates.removeAll(existingShotCoordinates);
			
			coordinatesUtil.removeOutOfBoundsCoordinates(targetCandidates, boardSize);
			
			log.debug("Target candidates: {}", targetCandidates);
			
			Collection<Coordinates> seaworthyCoordinates = coordinatesUtil.getCoordinates(hitsOnSeaworthyShips);
			int minShipLength = shipsUtil.getMinimumLenghtOfAliveShip(ships);
			
			List<Coordinates> candidates = coordinatesUtil.getAllCoordinates(boardSize); 
			candidates.removeAll(existingShotCoordinates);
			
			while (!targetCandidates.isEmpty()) {
				int randomIndex = randomUtil.getRandomInt(targetCandidates.size());
				Coordinates candidate = targetCandidates.get(randomIndex);
				
				if (coordinatesUtil.hasEnoughUnexploredOrSeaworthyNeighboursToFitSmallestSeaworthyShip(candidate, minShipLength, candidates, seaworthyCoordinates)) {
					return candidate;
				}
				
				targetCandidates.remove(candidate);
				candidates.remove(candidate);
				log.debug("Removed candidate: {} since smallest ship cannot fit around these coordinates.", candidate);
			}
		}
		
		return null;
	}

}
