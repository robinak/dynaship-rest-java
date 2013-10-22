package se.dynabyte.dynaship.service.getmove.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.LargestFirstComparator;
import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.ShotCollector;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.CoordinatesGroup;
import se.dynabyte.dynaship.service.getmove.model.CoordinatesGroup.Direction;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.model.State;
import se.dynabyte.dynaship.service.getmove.util.advanced.CoordinatesUtil;

/**
 * This strategy focus on finding existing hits on seaworthy ships
 *
 */
public class ExistingHitGameStateEvaluationStrategy implements GameStateEvaluationStrategy {
	
	private static final Logger log = LoggerFactory.getLogger(ExistingHitGameStateEvaluationStrategy.class);

	/**
	 * @return the coordinates of the next shot or {@code null} if no valid
	 *         target could be acquired near a seaworthy ship.
	 */
	@Override
	public Coordinates getMove(GameState gameState) {
		
		Collection<Shot> shots = gameState.getShots();
		Collection<Shot> hitsOnSeaworthyShips = new ShotCollector(shots).collect(State.SEAWORTHY);
		
		Set<CoordinatesGroup> groups = new HashSet<CoordinatesGroup>();
		
		for (Shot shot : hitsOnSeaworthyShips) {
			addCurrentCoordinatesToGroups(groups, shot.getCoordinates());
			mergeAdjacentGroups(groups);
		}
		
		Coordinates target = getTarget(groups, shots, gameState.getBoardSize());
		return target;
	}
	
	private void addCurrentCoordinatesToGroups(Collection<CoordinatesGroup> groups, Coordinates current) {
		
		//We need to create groups for directions in which c isn't part of any group.
		Collection<Direction> directionsToCreateNewGroupsForCoordinate = new ArrayList<Direction>(Arrays.asList(Direction.values()));
		
		for (CoordinatesGroup group : groups) {
			
			if (group.isNeighbourTo(current)) {
				group.add(current);
				directionsToCreateNewGroupsForCoordinate.remove(group.getDirection());
				log.debug("Adding coordinates {} to existing group with direction: {}", current, group.getDirection());
			}
		}
		
		for (Direction direction : directionsToCreateNewGroupsForCoordinate) {
			CoordinatesGroup group = new CoordinatesGroup(direction);
			group.add(current);
			groups.add(group);
			log.debug("Adding coordinates {} to new group with direction: {}", current, direction);
		}
	}

	private void mergeAdjacentGroups(Collection<CoordinatesGroup> groups) {
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
					
					log.debug("merging groups {} and {} to {}", group, g, mergedGroup);
				}
			}
		}
		
		groups.removeAll(groupsThatWereMerged);
		groups.addAll(newMergedGroups);
	}
	
	private Coordinates getTarget(Collection<CoordinatesGroup> groups, Collection<Shot> shots, int boardSize) {
		List<CoordinatesGroup> groupsLargestFirst = new ArrayList<CoordinatesGroup>(groups);
		sortGroupsBySizeInDecendingOrder(groupsLargestFirst);
		
		for (CoordinatesGroup group : groupsLargestFirst) {
			Collection<Coordinates> targetCandidates;
			
			if (group.size() == 1) {
				targetCandidates = getNonDiagonalNeighbours(group);
				
			} else {
				targetCandidates = getNeighboursInGoupDirection(group);
			}
			
			Collection<Coordinates> existingShotCoordinates = CoordinatesUtil.getCoordinates(shots);
			targetCandidates.removeAll(existingShotCoordinates);
			
			removeOutOfBoundsCoordinates(targetCandidates, boardSize);
			
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
			neighbours.add(new Coordinates(last.getX() + 1, last.getY()));
			break;
		case VERTICAL:
			neighbours.add(new Coordinates(first.getX(), first.getY() - 1));
			neighbours.add(new Coordinates(last.getX(), last.getY() + 1));
			break;
		}
		
		return neighbours;
	}

	
	private void removeOutOfBoundsCoordinates(Collection<Coordinates> coordinates, int boardSize) {
		
		for(Iterator<Coordinates> it = coordinates.iterator(); it.hasNext();) {
			Coordinates current = it.next();
			if (!isValid(current, 0, boardSize - 1)) {
				log.debug("Removing out of bounds coordinate {}", current);
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
