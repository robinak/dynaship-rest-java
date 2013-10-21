package se.dynabyte.dynaship.service.getmove.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;

public class ChainGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private final Collection<GameStateEvaluationStrategy> strategies;
	
	public ChainGameStateEvaluationStrategy(GameStateEvaluationStrategy... strategies) {
		this.strategies = new ArrayList<GameStateEvaluationStrategy>(Arrays.asList(strategies));
	}

	@Override
	public Coordinates getMove(GameState gameState) {
		
		for (GameStateEvaluationStrategy strategy : strategies) {
			Coordinates coordinates = strategy.getMove(gameState);
			if (coordinates != null) {
				return coordinates;
			}
		}
		
		return new BasicGameStateEvaluationStrategy().getMove(gameState);
	}

}
