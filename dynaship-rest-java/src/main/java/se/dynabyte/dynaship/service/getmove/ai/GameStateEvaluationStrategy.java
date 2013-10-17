package se.dynabyte.dynaship.service.getmove.ai;

import se.dynabyte.dynaship.service.getmove.model.Coordinate;
import se.dynabyte.dynaship.service.getmove.model.GameState;

/**
 * Interface for evaluating the current game state. Implement this interface and
 * set the "strategy" property in {@code get-move.yml} to the fully qualified
 * name of the implementation class to provide you own evaluation strategy.
 */
public interface GameStateEvaluationStrategy {

	/**
	 * Decides the coordinate of the next shot by evaluating the current game
	 * state.
	 * 
	 * @param gameState - the current game state.
	 * @return the {@link Coordinate} of the next shot.
	 */
	Coordinate getMove(GameState gameState);
}
