package se.dynabyte.dynaship.service.getmove.configuration;

import com.yammer.dropwizard.config.Configuration;

public class GetMoveConfiguration extends Configuration {
	
	private String strategy;
	
	/**
	 * Get the name of the game state evaluation strategy
	 * implementation class to use.
	 * 
	 * @return Fully qualified class name.
	 */
	public String getStrategy() {
		return strategy;
	}
	
}
