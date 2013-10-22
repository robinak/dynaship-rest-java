package se.dynabyte.dynaship.service.getmove.ai.advanced;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.BasicGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.ExistingHitGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Shot;

public class ChainGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(ExistingHitGameStateEvaluationStrategy.class);
	
	private final Collection<GameStateEvaluationStrategy> strategies;
	
	public ChainGameStateEvaluationStrategy(GameStateEvaluationStrategy... strategies) {
		this.strategies = Arrays.asList(strategies);
	}

	@Override
	public Coordinates getMove(GameState gameState) {
		
		Coordinates target = getTarget(gameState);
		
		printGameStateAndTarget(gameState, target);
		return target;
	}
	
	private Coordinates getTarget(GameState gameState) {
		for (GameStateEvaluationStrategy strategy : strategies) {
			log.debug("Evaluating with strategy {}", strategy);
			Coordinates coordinates = strategy.getMove(gameState);
			log.debug("Evaluation returned {}", coordinates);
			if (coordinates != null) {
				return coordinates;
			}
		}
		
		Coordinates coordinates = new BasicGameStateEvaluationStrategy().getMove(gameState);
		log.debug("Falling back to BasicGameStateEvaluationStrategy. Returning: {}", coordinates);
		return coordinates;
	}
	
	private void printGameStateAndTarget(GameState gameState, Coordinates target) {
		
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
