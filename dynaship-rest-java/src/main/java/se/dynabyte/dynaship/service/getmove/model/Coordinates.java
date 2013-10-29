package se.dynabyte.dynaship.service.getmove.model;

import javax.validation.constraints.Min;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a position on the game board.
 */
public class Coordinates implements Comparable<Coordinates> {

	@Min(0) private final int x;
	@Min(0) private final int y;

	@JsonCreator
	public Coordinates(
			@JsonProperty("x") int x, 
			@JsonProperty("y") int y) {
		
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean isNonDiagonalNeigbour(Coordinates other) {
		return isVerticalNeighbour(other) || isHorizontalNeighbour(other);
	}
	
	public boolean isVerticalNeighbour(Coordinates other) {
		boolean sameColumn = this.x == other.x;
		boolean columnNeighbour = sameColumn && Math.abs(this.y - other.y) == 1;
		return columnNeighbour;
	}
	
	public boolean isHorizontalNeighbour(Coordinates other) {
		boolean sameRow = this.y == other.y;
		boolean rowNeighbour = sameRow && Math.abs(this.x - other.x) == 1;
		return rowNeighbour;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Coordinates other = (Coordinates) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this,
				ToStringStyle.SHORT_PREFIX_STYLE).build();
	}

	@Override
	public int compareTo(Coordinates c) {
		if (this.y < c.y) {
			return -1;
			
		} else if (this.y > c.y) {
			return 1;
			
		} else {
			
			if (this.x < c.x) {
				return -1;
				
			} else if (this.x > c.x) {
				return 1;
				
			} else {
				return 0;
			}
			
		}
	}

}
