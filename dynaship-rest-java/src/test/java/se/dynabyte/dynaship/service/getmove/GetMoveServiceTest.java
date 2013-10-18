package se.dynabyte.dynaship.service.getmove;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import se.dynabyte.dynaship.service.getmove.configuration.GetMoveConfiguration;
import se.dynabyte.dynaship.service.getmove.resource.GetMoveResource;

import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class GetMoveServiceTest {
	
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
	public void run_adds_resource_to_environment() {
		service.run(configuration, environment);
		verify(environment).addResource(any(GetMoveResource.class));
	}
	
}
