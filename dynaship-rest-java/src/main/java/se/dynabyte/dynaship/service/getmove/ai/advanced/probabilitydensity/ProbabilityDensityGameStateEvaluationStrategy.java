package se.dynabyte.dynaship.service.getmove.ai.advanced.probabilitydensity;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.ShotCollector;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Ship;
import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.model.State;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroups;
import se.dynabyte.dynaship.service.getmove.model.advanced.probabilitydensity.DensityMap;
import se.dynabyte.dynaship.service.getmove.util.advanced.CoordinatesUtil;
import se.dynabyte.dynaship.service.getmove.util.advanced.Randomizer;

public class ProbabilityDensityGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(ProbabilityDensityGameStateEvaluationStrategy.class);
	
	private final CoordinatesUtil coordinatesUtil;
	private final Randomizer randomUtil;
	
	public ProbabilityDensityGameStateEvaluationStrategy(CoordinatesUtil coordinatesUtil, Randomizer randomUtil) {
		this.coordinatesUtil = coordinatesUtil;
		this.randomUtil = randomUtil;
	}

	@Override
	public Coordinates getMove(GameState gameState) {
		
		
		int boardSize = gameState.getBoardSize();
		
		//This algorithm is too slow with large game boards.
		if (boardSize > 15) {
			return null;
		}
		
		ShotCollector collector =  new ShotCollector(gameState.getShots());
		
		Collection<Shot> missedOrCapsizedShots = collector.collect(State.MISSED, State.CAPSIZED);
		Collection<Coordinates> missedOrCapsized = coordinatesUtil.getCoordinates(missedOrCapsizedShots);
		
		Collection<Coordinates> candidates = coordinatesUtil.getAllCoordinates(boardSize);
		candidates.removeAll(missedOrCapsized);
		
		CoordinatesGroups groups = new CoordinatesGroups();
		groups.addAllCoordinates(candidates, true);
		
		Collection<Shot> shotsOnSeaworthyShips = collector.collect(State.SEAWORTHY);
		Collection<Coordinates> seaworthyCoordinates = coordinatesUtil.getCoordinates(shotsOnSeaworthyShips);
		
		DensityMap densityMap = new DensityMap(boardSize, seaworthyCoordinates);
		
		
		for (Ship ship : gameState.getShips()) {
			if (ship.isAlive()) {
				
				CoordinatesGroups shipGroups = new CoordinatesGroups(groups);
				shipGroups.retainAll(ship.getLength());
				
				densityMap.incrementFor(shipGroups);
				
			}
		}
		
		log.debug("DensityMap: {}", densityMap);
		
		for(Coordinates c : seaworthyCoordinates) {
			densityMap.remove(c);
		}
		List<Coordinates> targetCandidates = densityMap.getCoordinatesForHighestDensity();
		
		if (!targetCandidates.isEmpty()) {
			int randomIndex = randomUtil.getRandomInt(targetCandidates.size());
			Coordinates target = targetCandidates.get(randomIndex);
			return target;
		} 
		
		return null;
	}
	

}
