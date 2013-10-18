package se.dynabyte.dynaship.service.getmove.ai;

import se.dynabyte.dynaship.service.getmove.GetMoveService;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.resource.GetMoveResource;

/**
 * Interface for evaluating the current game state.<br/>
 * <br/>
 * Implement this interface and provide an instance of the implementation class
 * as argument to the constructor of {@link GetMoveResource} in the run() method
 * of {@link GetMoveService} to provide your own evaluation logic, or modify the
 * implementation of the existing {@link BasicGameStateEvaluationStrategy}.
 */
public interface GameStateEvaluationStrategy {

	/**
	 * Decides the coordinate of the next shot by evaluating the current game
	 * state.
	 * 
	 * @param gameState - the current game state.
	 * @return the {@link Coordinates} of the next shot.
	 */
	Coordinates getMove(GameState gameState);
}
