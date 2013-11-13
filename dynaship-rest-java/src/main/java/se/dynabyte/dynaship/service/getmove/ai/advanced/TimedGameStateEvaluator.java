package se.dynabyte.dynaship.service.getmove.ai.advanced;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluator;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;

public class TimedGameStateEvaluator implements GameStateEvaluator {
	
	private static final Logger log = LoggerFactory.getLogger(TimedGameStateEvaluator.class);
	
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private final GameStateEvaluationStrategy strategy;
	private final long timeout;
	

	public TimedGameStateEvaluator(GameStateEvaluationStrategy strategy, long timeout) {
		this.strategy = strategy;
		this.timeout = timeout;
	}
	
	@Override
	public Coordinates evaluate(final GameState gameState) {
		
		Future<Coordinates> future = executor.submit(new Callable<Coordinates>() {
			@Override
			public Coordinates call() throws Exception {
				Coordinates target = strategy.getMove(gameState);
				return target;
			}
		});

		Coordinates target = null;
		try {
			log.debug("Evaluating with strategy {}. Allotted time {} ms.", strategy.getClass().getSimpleName(), timeout);
			target = future.get(timeout, TimeUnit.MILLISECONDS);
			log.debug("Evaluation with strategy {} returned {}", strategy.getClass().getSimpleName(), target);
	
		} catch (InterruptedException | ExecutionException e) {
			log.error("Thread exception for strategy: " + strategy.getClass().getSimpleName(), e);
	
		} catch (TimeoutException e) {
			log.warn("Timeout reached: {} ms when evaluating with strategy: {}, returning null", timeout, strategy.getClass().getSimpleName());
			future.cancel(true);
			
		}

		return target;
	}

}
