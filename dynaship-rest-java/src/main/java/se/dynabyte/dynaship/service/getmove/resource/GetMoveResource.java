package se.dynabyte.dynaship.service.getmove.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import se.dynabyte.dynaship.service.getmove.MediaType;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.model.Coordinate;
import se.dynabyte.dynaship.service.getmove.model.GameState;

import com.yammer.metrics.annotation.Timed;


@Path("/get-move")
@Consumes(MediaType.APPLICATION_JSON_UTF_8) 
@Produces(MediaType.APPLICATION_JSON_UTF_8) 
public class GetMoveResource {
	
	private GameStateEvaluationStrategy strategy;
	
	public GetMoveResource(GameStateEvaluationStrategy strategy) {
		this.strategy = strategy;
	}
	
	@POST @Timed
	public Coordinate getMove(GameState gameState) {
		return strategy.getMove(gameState);
	}

}
