package se.dynabyte.dynaship.service.getmove.configuration;

import com.yammer.dropwizard.config.Configuration;

public class GetMoveConfiguration extends Configuration {
	
	private String strategy;
	
	public String getStrategy() {
		return strategy;
	}
	
}
