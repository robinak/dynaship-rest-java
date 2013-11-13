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
		
		if (direction != group.direction) {
			return false;
		}
		
		return isNeighbourTo(group.first()) || isNeighbourTo(group.last());
		
	}
	
	public boolean isNeighbourTo(Coordinates coordinates) {
		
		switch(direction) {
		case HORIZONTAL: return (first().isHorizontalNeighbour(coordinates) || last().isHorizontalNeighbour(coordinates)) && !this.contains(coordinates);
		case VERTICAL: return (first().isVerticalNeighbour(coordinates) || last().isVerticalNeighbour(coordinates))&& !this.contains(coordinates);
		default: return false;
		}
		
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public enum Direction {
		HORIZONTAL,
		VERTICAL;
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof Coordinates) {
			Coordinates c = (Coordinates) o;
			return this.ceiling(c) != null && this.floor(c) != null;
		}
		return false;
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
