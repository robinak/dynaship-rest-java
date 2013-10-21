package se.dynabyte.dynaship.service.getmove.model;

import java.util.TreeSet;

public class CoordinatesGroup extends TreeSet<Coordinates> {

	private static final long serialVersionUID = 1L;
	
	public final Direction direction;
	
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
		for (Coordinates c : this) {
			if (c.isRowNeighbour(other)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isVerticalNeighbour(Coordinates other) {
		for (Coordinates c : this) {
			if (c.isColumnNeighbour(other)) {
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

}
