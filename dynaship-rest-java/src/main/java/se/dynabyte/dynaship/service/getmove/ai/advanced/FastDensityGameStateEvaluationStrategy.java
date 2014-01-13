package se.dynabyte.dynaship.service.getmove.ai.advanced;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Ship;
import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.model.advanced.probabilitydensity.DensityMap;
import se.dynabyte.dynaship.service.getmove.util.advanced.Randomizer;

public class FastDensityGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(FastDensityGameStateEvaluationStrategy.class);
	
	private Randomizer randomizer;
	
	public FastDensityGameStateEvaluationStrategy(Randomizer randomizer) {
		this.randomizer = randomizer;
	}

	@Override
	public Coordinates getMove(GameState gameState) {
		Map<Coordinates, Shot> shotMap = new HashMap<Coordinates, Shot>();
		for (Shot shot : gameState.getShots()) {
			shotMap.put(shot.getCoordinates(), shot);
		}
		int size = gameState.getBoardSize();
		DensityMap densityMap = new DensityMap(size, shotMap.keySet());
		
		
		for (Ship ship : gameState.getShips()) {
			int length = ship.getLength();
			
			//Try to place ship vertically
			xLoop :for (int x=0; x<size; x++) {
				yLoop: for (int y=0; y<=size-length; y++) {
				
					int[] shipDensities = new int[length];
					int seaworthyBonus = 0;
					shipLoop: for (int k=y+length-1; k>=y; k--) { //Reverse ship loop so we can maximize jump if ship can't be placed
			
						Coordinates these = new Coordinates(x, k);
						int density = densityMap.containsKey(these) ? densityMap.get(these) : 0;
						Shot shot = shotMap.get(these);
						if(shot != null) {
							switch(shot.getState()) {
							case CAPSIZED:
							case MISSED: y += k-y; continue yLoop; //Jump ahead the length of the part of ship that has been examined and continue
							case SEAWORTHY: seaworthyBonus += size * size;
							}
						} else {
							density++;
						}
						shipDensities[k-y] = density;
					}
					shipDensityLoop: for (int i=0; i<shipDensities.length; i++) {
						Coordinates these = new Coordinates(x, y+i);
						densityMap.put(these, shipDensities[i] + seaworthyBonus);
					}
				}
			}
			
			//Try to place ship horizontally
			yLoop: for (int y=0; y<size; y++) {
				xLoop :for (int x=0; x<=size-length; x++) {
					
					int[] shipDensities = new int[length];
					int seaworthyBonus = 0;
					shipLoop: for (int k=x+length-1; k>=x; k--) { //Reverse ship loop so we can maximize jump if ship can't be placed
			
						Coordinates these = new Coordinates(k, y);
						int density = densityMap.containsKey(these) ? densityMap.get(these) : 0;
						Shot shot = shotMap.get(these);
						if(shot != null) {
							switch(shot.getState()) {
							case CAPSIZED:
							case MISSED:  x += k-x; continue xLoop; //Jump ahead the length of the part of ship that has been examined and continue
							case SEAWORTHY: seaworthyBonus += size * size;
							}
						} else {
							density++;
						}
						shipDensities[k-x] = density;
					}
					shipDensityLoop: for (int i=0; i<shipDensities.length; i++) {
						Coordinates these = new Coordinates(x+i, y);
						densityMap.put(these, shipDensities[i] + seaworthyBonus);
					}
			
				}
			}
		}
		
		log.debug(densityMap.toString());
		List<Coordinates> highestDensityCoordinates = densityMap.getHighestDensityCoordinates();
		
		if (!highestDensityCoordinates.isEmpty()) {
			int index = randomizer.getRandomInt(highestDensityCoordinates.size());
			return highestDensityCoordinates.get(index);	
		}
		return null;
	}

}
