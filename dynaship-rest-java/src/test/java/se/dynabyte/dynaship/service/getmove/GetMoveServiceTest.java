package se.dynabyte.dynaship.service.getmove;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import se.dynabyte.dynaship.service.getmove.configuration.GetMoveConfiguration;
import se.dynabyte.dynaship.service.getmove.resource.GetMoveResource;

import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class GetMoveServiceTest {
	
	private static final String STRATEGY_CLASS_NAME = "se.dynabyte.dynaship.service.getmove.ai.BasicGameStateEvaluationStrategy";
	
	@InjectMocks private GetMoveService service = new GetMoveService();
	
	@Mock Bootstrap<GetMoveConfiguration> bootstrap;
	@Mock Environment environment;
	@Mock GetMoveConfiguration configuration;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void initialize_sets_name_on_bootstrap() {
		service.initialize(bootstrap);
		verify(bootstrap).setName("get-move");
	}
	
	@Test
	public void run_adds_resource_with_configurated_strategy_to_environment() {
		when(configuration.getStrategy()).thenReturn(STRATEGY_CLASS_NAME);
		
		service.run(configuration, environment);
		
		verifyResourceWithConfiguredStrategyAddedToEnvironment();
	}

	private void verifyResourceWithConfiguredStrategyAddedToEnvironment() {
		ArgumentCaptor<GetMoveResource> argument = ArgumentCaptor.forClass(GetMoveResource.class);
		verify(environment).addResource(argument.capture());
		assertEquals(STRATEGY_CLASS_NAME, argument.getValue().getStategy().getClass().getName());
	}
	
}
