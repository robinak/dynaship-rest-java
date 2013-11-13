package se.dynabyte.dynaship.service.getmove.ai;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;

public interface GameStateEvaluator {
	
	Coordinates evaluate(GameState gameState);

}
