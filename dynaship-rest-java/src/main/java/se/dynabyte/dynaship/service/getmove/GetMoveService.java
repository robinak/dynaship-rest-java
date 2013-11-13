package se.dynabyte.dynaship.service.getmove;


import se.dynabyte.dynaship.service.getmove.ai.BasicGameStateEvaluator;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluator;
import se.dynabyte.dynaship.service.getmove.ai.advanced.ChainGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.advanced.FirstGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.advanced.SimpleGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.ExistingHitGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.advanced.probabilitydensity.ProbabilityDensityGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.configuration.GetMoveConfiguration;
import se.dynabyte.dynaship.service.getmove.resource.GetMoveResource;
import se.dynabyte.dynaship.service.getmove.util.advanced.CoordinatesUtil;
import se.dynabyte.dynaship.service.getmove.util.advanced.GameStateLogger;
import se.dynabyte.dynaship.service.getmove.util.advanced.Randomizer;
import se.dynabyte.dynaship.service.getmove.util.advanced.ShipsUtil;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class GetMoveService extends Service<GetMoveConfiguration> {
	
	private static final ShipsUtil shipsUtil = new ShipsUtil();
	private static final CoordinatesUtil coordinatesUtil = new CoordinatesUtil();
	private static final Randomizer randomUtil = new Randomizer();
	private static final GameStateLogger gameStateLogger = new GameStateLogger();
	
    public static void main(String[] args) throws Exception {
        new GetMoveService().run(args);
    }

    @Override
    public void initialize(Bootstrap<GetMoveConfiguration> bootstrap) {
        bootstrap.setName("get-move");
    }
    
	@Override
    public void run(GetMoveConfiguration configuration, Environment environment) {
		
		long timeout = configuration.getTimeout();
		
		GameStateEvaluationStrategy first = new FirstGameStateEvaluationStrategy();
		GameStateEvaluationStrategy density = new ProbabilityDensityGameStateEvaluationStrategy(shipsUtil, coordinatesUtil, randomUtil);
		GameStateEvaluationStrategy existingHit = new ExistingHitGameStateEvaluationStrategy(shipsUtil, coordinatesUtil, randomUtil);
		GameStateEvaluationStrategy simple = new SimpleGameStateEvaluationStrategy(shipsUtil, coordinatesUtil, randomUtil);
		GameStateEvaluationStrategy strategy = new ChainGameStateEvaluationStrategy(timeout, gameStateLogger, first, density, existingHit, simple);

		GameStateEvaluator evaluator = new BasicGameStateEvaluator(strategy);
		GetMoveResource resource = new GetMoveResource(evaluator);
       
		environment.addResource(resource);
    }

}
