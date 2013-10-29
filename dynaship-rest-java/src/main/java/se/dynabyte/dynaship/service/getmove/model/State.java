package se.dynabyte.dynaship.service.getmove.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {
	SEAWORTHY, 
	MISSED, 
	CAPSIZED;

	@JsonCreator
	public static State fromString(String string) {

		for (State state : State.values()) {
			if (state.name().equalsIgnoreCase(string)) {
				return state;
			}
		}
		throw new IllegalArgumentException("No state corresponding to [" + string + "].");
	}
	
}
