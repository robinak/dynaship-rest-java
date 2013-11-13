package se.dynabyte.dynaship.service.getmove.ai.advanced;

import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;

public class FirstGameStateEvaluationStrategy implements GameStateEvaluationStrategy {

	@Override
	public Coordinates getMove(GameState gameState) {
		
		if (gameState.getShots().isEmpty()) {
			int center = Math.round(gameState.getBoardSize() / 2);
			return new Coordinates(center, center);
		}
		return null;
	}

}
