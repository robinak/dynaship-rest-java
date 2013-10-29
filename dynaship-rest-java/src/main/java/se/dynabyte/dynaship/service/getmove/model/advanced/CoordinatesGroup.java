package se.dynabyte.dynaship.service.getmove.model.advanced;

import java.util.TreeSet;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;

public class CoordinatesGroup extends TreeSet<Coordinates> {

	private static final long serialVersionUID = 1L;
	
	private final Direction direction;
	
	public CoordinatesGroup(Direction direction) {
		this.direction = direction;
	}
	
	public boolean isNeighbourTo(CoordinatesGroup group) {
		for (Coordinates c : group) {
			if (isNeighbourTo(c)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isNeighbourTo(Coordinates coordinates) {
		switch(direction) {
		case HORIZONTAL: return isHorizontalNeighbour(coordinates);
		case VERTICAL: return isVerticalNeighbour(coordinates);
		default: return false;
		}
		
	}
	
	private boolean isHorizontalNeighbour(Coordinates other) {
		if (this.contains(other)) {
			return false;
		}
		
		for (Coordinates c : this) {
			if (c.isHorizontalNeighbour(other)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isVerticalNeighbour(Coordinates other) {
		if (this.contains(other)) {
			return false;
		}
		
		for (Coordinates c : this) {
			if (c.isVerticalNeighbour(other)) {
				return true;
			}
		}
		return false;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public enum Direction {
		HORIZONTAL,
		VERTICAL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CoordinatesGroup other = (CoordinatesGroup) obj;
		if (direction != other.direction) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder();
		description.append("[Direction: ");
		description.append(direction);
		description.append(", Coordinates: ");
		description.append(super.toString());
		description.append("]");
		
		return description.toString();
	}

}
