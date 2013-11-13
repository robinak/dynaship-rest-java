package se.dynabyte.dynaship.service.getmove.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class GetMoveConfiguration extends Configuration {
	
	@JsonProperty
    private long timeout;
	
	public long getTimeout() {
		return timeout;
	}
}
