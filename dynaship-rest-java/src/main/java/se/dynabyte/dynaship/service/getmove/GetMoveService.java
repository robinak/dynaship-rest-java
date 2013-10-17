package se.dynabyte.dynaship.service.getmove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.BasicGameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.ai.GameStateEvaluationStrategy;
import se.dynabyte.dynaship.service.getmove.configuration.GetMoveConfiguration;
import se.dynabyte.dynaship.service.getmove.resource.GetMoveResource;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class GetMoveService extends Service<GetMoveConfiguration> {
	
	private static final Logger log = LoggerFactory.getLogger(GetMoveService.class);
	
    public static void main(String[] args) throws Exception {
        new GetMoveService().run(args);
    }

    @Override
    public void initialize(Bootstrap<GetMoveConfiguration> bootstrap) {
        bootstrap.setName("get-move");
    }

	@Override
    public void run(GetMoveConfiguration configuration, Environment environment) {
		
		GameStateEvaluationStrategy strategy = getStrategy(configuration);
        environment.addResource(new GetMoveResource(strategy));
    }

	private GameStateEvaluationStrategy getStrategy(
			GetMoveConfiguration configuration) {
		GameStateEvaluationStrategy strategy;
		try {
			Class<?> strategyClass = Class.forName(configuration.getStrategy());
			strategy = (GameStateEvaluationStrategy) strategyClass.newInstance();
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.warn("Failed to instantiate strategy class [{}] from configuration. Using default strategy [" + BasicGameStateEvaluationStrategy.class.getName() + "] instead.", configuration.getStrategy());
			strategy = new BasicGameStateEvaluationStrategy();
		}
		return strategy;
	}
}
