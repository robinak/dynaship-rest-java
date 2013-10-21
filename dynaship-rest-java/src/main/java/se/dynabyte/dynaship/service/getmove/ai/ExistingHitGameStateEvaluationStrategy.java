package se.dynabyte.dynaship.service.getmove.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.LargestFirstComparator;
import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.ShotCollector;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.CoordinatesGroup;
import se.dynabyte.dynaship.service.getmove.model.CoordinatesGroup.Direction;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.model.State;

/**
 * This strategy focus on finding existing hits on seaworthy ships
 *
 */
public class ExistingHitGameStateEvaluationStrategy implements GameStateEvaluationStrategy {

	/**
	 * @return the coordinates of the next shot or {@code null} if no valid
	 *         target could be acquired near a seaworthy ship.
	 */
	@Override
	public Coordinates getMove(GameState gameState) {
		
		ShotCollector collector = new ShotCollector(gameState.getShots());
		
		Collection<Shot> hitsOnUnsunkenShips = collector.collect(State.SEAWORTHY);
		
		
		Collection<CoordinatesGroup> groups = new ArrayList<CoordinatesGroup>();
		
		for (Shot shot : hitsOnUnsunkenShips) {
			Coordinates c = shot.getCoordinates();
			
			//We need to create groups for directions in which c isn't part of any group.
			Collection<Direction> directionsToCreateNewGroups = new ArrayList<Direction>(Arrays.asList(Direction.values()));
			
			for (CoordinatesGroup group : groups) {
				
				if (group.isNeighbourTo(c)) {
					group.add(c);
					directionsToCreateNewGroups.remove(group.getDirection());
				}
			}
			
			for (Direction direction : directionsToCreateNewGroups) {
				CoordinatesGroup group = new CoordinatesGroup(direction);
				group.add(c);
				groups.add(group);
			}
			
			Collection<CoordinatesGroup> groupsThatWereMerged = new ArrayList<CoordinatesGroup>();
			Collection<CoordinatesGroup> newMergedGroups = new ArrayList<CoordinatesGroup>();
			//Go through groups again and see if any groups are neighbours. If so, merge them.
			for (CoordinatesGroup group : groups) {
				for (CoordinatesGroup g : groups) {
					if (group.isNeighbourTo(g)) {
						
						CoordinatesGroup mergedGroup = new CoordinatesGroup(group.getDirection());
						mergedGroup.addAll(group);
						mergedGroup.addAll(g);
						newMergedGroups.add(mergedGroup);
						
						groupsThatWereMerged.add(group);
						groupsThatWereMerged.add(g);
					}
				}
			}
			
			groups.removeAll(groupsThatWereMerged);
			groups.addAll(newMergedGroups);
			
		}
		
		Coordinates target = getTarget(groups, gameState);
		return target;
	}
	
	private Coordinates getTarget(Collection<CoordinatesGroup> groups, GameState gameState) {
		List<CoordinatesGroup> groupsLargestFirst = new ArrayList<CoordinatesGroup>(groups);
		sortGroupsBySizeInDecendingOrder(groupsLargestFirst);
		
		Collection<Coordinates> targetCandidates;
		
		for (CoordinatesGroup group : groupsLargestFirst) {
			
			if (group.size() == 1) {
				targetCandidates = getNonDiagonalNeighbours(group);
				
			} else {
				targetCandidates = getNeighboursInGoupDirection(group);
			}
			
			Collection<Coordinates> shotCoordinates = getShotCoordinates(gameState.getShots());
			targetCandidates.removeAll(shotCoordinates);
			
			removeCoordinatesOutOfBounds(targetCandidates, gameState.getBoardSize());
			
			if (!targetCandidates.isEmpty()) {
				return targetCandidates.iterator().next();
			}
		}
		
		return null;
	}
	
	private void sortGroupsBySizeInDecendingOrder(List<CoordinatesGroup> groups) {
		Collections.sort(groups, new LargestFirstComparator());
	}
	
	private Collection<Coordinates> getNonDiagonalNeighbours(CoordinatesGroup group) {
		
		Coordinates center = group.first();
		
		Coordinates up = new Coordinates(center.getX(), center.getY() -1);
		Coordinates right = new Coordinates(center.getX() +1, center.getY());
		Coordinates down = new Coordinates(center.getX(), center.getY() + 1);
		Coordinates left = new Coordinates(center.getX() -1, center.getY());
		
		Collection<Coordinates> neighbours = new ArrayList<Coordinates>();
		neighbours.add(up);
		neighbours.add(right);
		neighbours.add(down);
		neighbours.add(left);
		
		return neighbours;
	}
	
	private Collection<Coordinates> getNeighboursInGoupDirection(CoordinatesGroup group) {
		
		Collection<Coordinates> neighbours = new ArrayList<Coordinates>();
		
		Coordinates first = group.first();
		Coordinates last = group.last();
		
		switch(group.getDirection()) {
		case HORIZONTAL: 
			neighbours.add(new Coordinates(first.getX() - 1, first.getY()));
			neighbours.add(new Coordinates(last.getX() + 1, first.getY()));
			break;
		case VERTICAL:
			neighbours.add(new Coordinates(first.getX(), first.getY() - 1));
			neighbours.add(new Coordinates(last.getX(), first.getY() + 1));
			break;
		}
		
		return neighbours;
	}
	
	private Collection<Coordinates> getShotCoordinates(Collection<Shot> shots) {
		Collection<Coordinates> shotCoordinates = new ArrayList<Coordinates>();
		
		for (Shot shot : shots) {
			shotCoordinates.add(shot.getCoordinates());
		}
		
		return shotCoordinates;
	}
	
	private void removeCoordinatesOutOfBounds(Collection<Coordinates> coordinates, int boardSize) {
		
		for(Iterator<Coordinates> it = coordinates.iterator(); it.hasNext();) {
			Coordinates current = it.next();
			if (!isValid(current, 0, boardSize - 1)) {
				it.remove();
			}
		}
		
	}
	
	private boolean isValid(Coordinates coordinates, int min, int max) {
		int x = coordinates.getX();
		int y = coordinates.getY();		
		return x >= min && y >= min && x <= max && y <= max;
	}

}
