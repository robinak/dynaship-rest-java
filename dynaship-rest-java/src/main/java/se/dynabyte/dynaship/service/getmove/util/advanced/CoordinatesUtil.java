package se.dynabyte.dynaship.service.getmove.util.advanced;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroups;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup.Direction;

public class CoordinatesUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CoordinatesUtil.class);
	
	public List<Coordinates> getCoordinates(Collection<Shot> shots) {
		List<Coordinates> shotCoordinates = new ArrayList<Coordinates>();
		
		for (Shot shot : shots) {
			shotCoordinates.add(shot.getCoordinates());
		}
		
		return shotCoordinates;
	}
	
	public List<Coordinates> getAllCoordinates(int boardSize) {
		List<Coordinates> allCoordinates = new ArrayList<Coordinates>();
		
		for(int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				allCoordinates.add(new Coordinates(j, i));
			}
		}
		return allCoordinates;
	}
	
	public List<Coordinates> getNonDiagonalNeighbours(Coordinates coordinates) {
		
		Coordinates up = new Coordinates(coordinates.getX(), coordinates.getY() -1);
		Coordinates right = new Coordinates(coordinates.getX() +1, coordinates.getY());
		Coordinates down = new Coordinates(coordinates.getX(), coordinates.getY() + 1);
		Coordinates left = new Coordinates(coordinates.getX() -1, coordinates.getY());
		
		List<Coordinates> neighbours = new ArrayList<Coordinates>();
		neighbours.add(up);
		neighbours.add(right);
		neighbours.add(down);
		neighbours.add(left);
		
		log.debug("Getting non diagonal neighbours for {}, result: {}", coordinates, neighbours);
		return neighbours;
	}
	
	public void removeOutOfBoundsCoordinates(Collection<Coordinates> coordinates, int boardSize) {
		
		for(Iterator<Coordinates> it = coordinates.iterator(); it.hasNext();) {
			Coordinates current = it.next();
			if (!isValid(current, 0, boardSize - 1)) {
				log.debug("Removing out of bounds coordinates {}", current);
				it.remove();
			}
		}
	}
	
	private boolean isValid(Coordinates coordinates, int min, int max) {
		int x = coordinates.getX();
		int y = coordinates.getY();		
		return x >= min && y >= min && x <= max && y <= max;
	}
	
	public List<Coordinates> getNeighboursInGoupDirection(CoordinatesGroup group) {
		
		List<Coordinates> neighbours = new ArrayList<Coordinates>();
		
		Coordinates first = group.first();
		Coordinates last = group.last();
		
		Direction direction = group.getDirection();
		switch(direction) {
		case HORIZONTAL: 
			neighbours.add(new Coordinates(first.getX() - 1, first.getY()));
			neighbours.add(new Coordinates(last.getX() + 1, last.getY()));
			break;
		case VERTICAL:
			neighbours.add(new Coordinates(first.getX(), first.getY() - 1));
			neighbours.add(new Coordinates(last.getX(), last.getY() + 1));
			break;
		}
		
		log.debug("Getting directional neighbours for group: {}, result: {}", group, neighbours);
		return neighbours;
	}
	
	public boolean hasEnoughUnexploredOrSeaworthyNeighboursToFitSmallestSeaworthyShip(Coordinates coordinates, int minShipLength, Collection<Coordinates> candidates, Collection<Coordinates> seaworthyCoordinates) {
		
		CoordinatesGroups groups = new CoordinatesGroups();
		
		Collection<Coordinates> candidatesAndHitsOnSeaworthyShips = new ArrayList<Coordinates>();
		candidatesAndHitsOnSeaworthyShips.addAll(candidates);
		candidatesAndHitsOnSeaworthyShips.addAll(seaworthyCoordinates);
		
		groups.addAllCoordinates(candidatesAndHitsOnSeaworthyShips);
		groups.mergeAdjacent();
		
		return groups.contains(coordinates, minShipLength);
	}

}
