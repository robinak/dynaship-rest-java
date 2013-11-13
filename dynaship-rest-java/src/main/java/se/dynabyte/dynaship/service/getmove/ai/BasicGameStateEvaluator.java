package se.dynabyte.dynaship.service.getmove.ai;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;

public class BasicGameStateEvaluator implements GameStateEvaluator {
	
	private final GameStateEvaluationStrategy strategy;
	

	public BasicGameStateEvaluator(GameStateEvaluationStrategy strategy) {
		this.strategy = strategy;
	}


	@Override
	public Coordinates evaluate(GameState gameState) {
		return strategy.getMove(gameState);
	}

}
