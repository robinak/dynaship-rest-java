package se.dynabyte.dynaship.service.getmove.ai.advanced;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.BasicGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.ExistingHitGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.util.advanced.GameStateLogger;

public class ChainGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(ExistingHitGameStateEvaluationStrategy.class);
	
	private final GameStateLogger gameStateLogger;
	private final Collection<GameStateEvaluationStrategy> strategies;
	
	public ChainGameStateEvaluationStrategy(GameStateLogger gameStateLogger, GameStateEvaluationStrategy... strategies) {
		this.gameStateLogger = gameStateLogger;
		this.strategies = Arrays.asList(strategies);
	}

	@Override
	public Coordinates getMove(GameState gameState) {
		Coordinates target = getTarget(gameState);
		gameStateLogger.log(gameState, target);
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

}
