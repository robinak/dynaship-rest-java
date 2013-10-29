package se.dynabyte.dynaship.service.getmove.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Shot {
	
	private final Coordinates coordinate;
	private final State state;
	private final int shipId;
	
	@JsonCreator
	public Shot(
			@JsonProperty("coordinates") Coordinates coordinates,
			@JsonProperty("shipState") State state,
			@JsonProperty("shipId") int shipId) {
		this.coordinate = coordinates;
		this.state = state;
		this.shipId = shipId;
	}

	public Coordinates getCoordinate() {
		return coordinate;
	}

	public State getState() {
		return state;
	}

	public int getShipId() {
		return shipId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinate == null) ? 0 : coordinate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Shot other = (Shot) obj;
		if (coordinate == null) {
			if (other.coordinate != null) {
				return false;
			}
		} else if (!coordinate.equals(other.coordinate)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).build();
	}

}
