package se.dynabyte.dynaship.service.getmove.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Shot {
	
	@Valid @NotNull private final Coordinates coordinates;
	@NotNull private final State state;
	private final int shipId;
	
	@JsonCreator
	public Shot(
			@JsonProperty("coordinates") Coordinates coordinates,
			@JsonProperty("shipState") State state,
			@JsonProperty("shipId") int shipId) {
		
		this.coordinates = coordinates;
		this.state = state;
		this.shipId = shipId;
	}

	public Coordinates getCoordinates() {
		return coordinates;
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
				+ ((coordinates == null) ? 0 : coordinates.hashCode());
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
		if (coordinates == null) {
			if (other.coordinates != null) {
				return false;
			}
		} else if (!coordinates.equals(other.coordinates)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).build();
	}

}
