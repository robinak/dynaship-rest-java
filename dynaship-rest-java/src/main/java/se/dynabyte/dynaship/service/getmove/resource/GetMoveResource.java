package se.dynabyte.dynaship.service.getmove.resource;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.MediaType;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluator;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;

import com.yammer.metrics.annotation.Timed;


@Path("/get-move")
@Consumes(MediaType.APPLICATION_JSON_UTF_8) 
@Produces(MediaType.APPLICATION_JSON_UTF_8)
public class GetMoveResource {
	
	private static final Logger log = LoggerFactory.getLogger(GetMoveResource.class);
	
	private final GameStateEvaluator evaluator;
	
	public GetMoveResource(GameStateEvaluator evaluator) {
		this.evaluator = evaluator;
	}
	
	@POST @Timed
	public Coordinates getMove(@Valid GameState gameState) {
		Coordinates target = evaluator.evaluate(gameState);
		log.info("Final target: {}", target);
		return target;
	}

}
