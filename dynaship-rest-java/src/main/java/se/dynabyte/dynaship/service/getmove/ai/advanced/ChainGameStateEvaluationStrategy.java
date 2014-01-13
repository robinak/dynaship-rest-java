package se.dynabyte.dynaship.service.getmove.ai.advanced;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.BasicGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.BasicGameStateEvaluator;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.util.advanced.GameStateLogger;

public class ChainGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(ChainGameStateEvaluationStrategy.class);
	
	private final long timeout;
	private final GameStateLogger gameStateLogger;
	private final Collection<GameStateEvaluationStrategy> strategies;
	
	public ChainGameStateEvaluationStrategy(long timeout, GameStateLogger gameStateLogger, GameStateEvaluationStrategy... strategies) {
		this.timeout = timeout;
		this.gameStateLogger = gameStateLogger;
		this.strategies = Arrays.asList(strategies);
	}

	@Override
	public Coordinates getMove(GameState gameState) {
		
		Coordinates target = null;
		long remainingTime = timeout;
		
		long startTime = System.currentTimeMillis();
		for (GameStateEvaluationStrategy strategy : strategies) {
			
			long subStartTime = System.currentTimeMillis();
			if (subStartTime - startTime > timeout - 50) {
				break;
			}
			
			long subTimeout = (long) (0.7 * remainingTime);
			
//			target = new TimedGameStateEvaluator(strategy, subTimeout).evaluate(gameState);
			target = new BasicGameStateEvaluator(strategy).evaluate(gameState);
			
			long elapsedTime = System.currentTimeMillis() - subStartTime;
			remainingTime -= elapsedTime;
			
			if (target != null) {
				break;
			}
		}
		
		if (target == null) {
			target = new BasicGameStateEvaluationStrategy().getMove(gameState);
			log.debug("Falling back to BasicGameStateEvaluationStrategy, returning: {}", target);
		}
		
		gameStateLogger.log(gameState, target);
		return target;
	}
	
}
