package se.dynabyte.dynaship.service.getmove.util.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit.LargestFirstComparator;
import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup.Direction;

public class CoordinatesGroupUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CoordinatesGroupUtil.class);
	
	public void addCoordinatesToGroups(Coordinates coordinates, Collection<CoordinatesGroup> groups) {
		
		//We need to create groups for directions in which c isn't part of any group.
		Collection<Direction> directionsToCreateNewGroupsForCoordinate = new ArrayList<Direction>(Arrays.asList(Direction.values()));
		
		for (CoordinatesGroup group : groups) {
			
			if (group.isNeighbourTo(coordinates)) {
				log.debug("Adding {} to existing group {}", coordinates, group);
				group.add(coordinates);
				directionsToCreateNewGroupsForCoordinate.remove(group.getDirection());
			}
		}
		
		for (Direction direction : directionsToCreateNewGroupsForCoordinate) {
			log.debug("Adding {} to new group with direction: {}", coordinates, direction);
			CoordinatesGroup group = new CoordinatesGroup(direction);
			group.add(coordinates);
			groups.add(group);
		}
	}
	
	public Collection<CoordinatesGroup> mergeAdjacentGroups(Collection<CoordinatesGroup> groups) {
		
		log.debug("mergeAdjacentGroups called with: {}", groups);
		
		Collection<CoordinatesGroup> mergedGroups = new HashSet<CoordinatesGroup>(groups);
		
		boolean executeMerge = false;
		Collection<CoordinatesGroup> groupsThatWereMerged = new HashSet<CoordinatesGroup>();
		Collection<CoordinatesGroup> newMergedGroups = new HashSet<CoordinatesGroup>();
		//Go through groups again and see if any groups are neighbours. If so, merge them.
		for (CoordinatesGroup outerGroup : mergedGroups) {
			for (CoordinatesGroup innerGroup : mergedGroups) {
				if (shouldBeMerged(outerGroup, innerGroup, groupsThatWereMerged)) {
					
					CoordinatesGroup mergedGroup = new CoordinatesGroup(outerGroup.getDirection());
					mergedGroup.addAll(outerGroup);
					mergedGroup.addAll(innerGroup);
					newMergedGroups.add(mergedGroup);
					
					groupsThatWereMerged.add(outerGroup);
					groupsThatWereMerged.add(innerGroup);
					log.debug("merging groups {} and {} to {}", outerGroup, innerGroup, mergedGroup);
					
					executeMerge = true;
				}
			}
		}
		
		if (executeMerge) {
			mergedGroups.removeAll(groupsThatWereMerged);
			mergedGroups.addAll(newMergedGroups);
			mergedGroups = mergeAdjacentGroups(mergedGroups);
		}
		
		return mergedGroups;
		
	}
	
	private boolean shouldBeMerged(CoordinatesGroup g1, CoordinatesGroup g2, Collection<CoordinatesGroup> groupsThatWereMerged) {
		return (g1.isNeighbourTo(g2) || isSubSet(g1, g2)) && hasSameDirection(g1, g2) && !isSame(g1, g2);
	}

	private boolean isSame(CoordinatesGroup g1, CoordinatesGroup g2) {
		return g1 == g2;
	}
	
	private boolean isSubSet(CoordinatesGroup g1, CoordinatesGroup g2) {
		return g1.containsAll(g2) || g2.containsAll(g1);
	}
	
	private boolean hasSameDirection(CoordinatesGroup g1, CoordinatesGroup g2) {
		return g1.getDirection() == g2.getDirection();
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
	
	public void sortGroupsBySizeInDecendingOrder(List<CoordinatesGroup> groups) {
		Collections.sort(groups, new LargestFirstComparator());
	}

}
