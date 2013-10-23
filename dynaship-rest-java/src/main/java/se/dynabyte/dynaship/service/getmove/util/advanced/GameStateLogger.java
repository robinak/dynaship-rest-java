package se.dynabyte.dynaship.service.getmove.util.advanced;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Shot;

public class GameStateLogger {
	
	private static final Logger log = LoggerFactory.getLogger(GameStateLogger.class);
	
	/**
	 * Logs a visual representation of the game state and target.
	 * @param gameState - the game state to be logged.
	 * @param target - the target coordinates.
	 */
	public void log(GameState gameState, Coordinates target) {
		
		StringBuilder board = new StringBuilder();
		board.append("Game state and target:\n");
		
		Map<Coordinates, Shot> shots = new HashMap<Coordinates, Shot>();
		for (Shot shot : gameState.getShots()) {
			shots.put(shot.getCoordinates(), shot);
		}
		
		int size = gameState.getBoardSize();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Coordinates c = new Coordinates(j, i);
				
				char ch = '_';
				if (shots.containsKey(c)) {
					Shot shot = shots.get(c);
					switch(shot.getState()) {
					case SEAWORTHY: ch = 'S'; break;
					case MISSED: ch = 'M'; break;
					case CAPSIZED: ch = 'C'; break;
					}
				}
				
				if(c.equals(target)) {
					ch = 'T';
				}
				board.append(ch);
				board.append(" ");
				
			}
			board.append("\n");
		}
		log.debug(board.toString());
	}

}
