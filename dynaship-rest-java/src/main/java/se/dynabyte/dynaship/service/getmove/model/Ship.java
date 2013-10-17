package se.dynabyte.dynaship.service.getmove.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ship {
	
	private final int id;
	private final int length;
	private final boolean alive;
	
	@JsonCreator
	public Ship(
			@JsonProperty("id") int id, 
			@JsonProperty("length") int lenght, 
			@JsonProperty("alive") boolean alive) {
		
		this.id = id;
		this.length = lenght;
		this.alive = alive;
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public boolean isAlive() {
		return alive;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Ship other = (Ship) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).build();
	}
	
}
