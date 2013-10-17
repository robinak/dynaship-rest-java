package se.dynabyte.dynaship.service.getmove.ai;

import se.dynabyte.dynaship.service.getmove.model.Coordinate;
import se.dynabyte.dynaship.service.getmove.model.GameState;

public interface GameStateEvaluationStrategy {

		Coordinate getMove(GameState gameState);
}
