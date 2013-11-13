package se.dynabyte.dynaship.service.getmove.model.advanced.probabilitydensity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroups;

public class DensityMap extends HashMap<Coordinates, Integer> {

	private static final long serialVersionUID = 1L;
	
	private final int boardSize;
	private final Collection<Coordinates> seaworthyCoordinates;
	private final List<Coordinates> highestDensityCoordinates;
	
	public DensityMap(int boardSize, Collection<Coordinates> seaworthyCoordinates) {
		this.boardSize = boardSize;
		this.seaworthyCoordinates = new HashSet<Coordinates>(seaworthyCoordinates);
		this.highestDensityCoordinates = new ArrayList<Coordinates>();
		
		for(int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				put(new Coordinates(j, i), 0);
			}
		}
	}
	
	public void incrementFor(CoordinatesGroups coordinatesGroups) {
		for (CoordinatesGroup group : coordinatesGroups) {
			int seaworthyBonus = 0;
					
			for (Coordinates coordinates : group) {
				
				if(isSeaworty(coordinates)) {
					seaworthyBonus += boardSize * boardSize;
				}
				
			}
			
			//Add the group's total seaworthy bonus to all coordinates in group plus overlap bonus
			for (Coordinates coordinates : group) {
				int overlapBonus = 10;
				int oldDensity = get(coordinates);
				int newDensity = oldDensity + seaworthyBonus + overlapBonus;
				put(coordinates, newDensity);
			}
			
		}
	}

	private boolean isSeaworty(Coordinates coordinates) {
		return seaworthyCoordinates.contains(coordinates);
	}
	
	@Override
	public Integer put(Coordinates coordinates, Integer density) {
		int d = density == null || isSeaworty(coordinates) ? 0 : density;
		updateHighestDensity(coordinates, d);
		return super.put(coordinates, d);
	}
	
	private void updateHighestDensity(Coordinates coordinates, int density) {
		if (highestDensityCoordinates.isEmpty()) {
			highestDensityCoordinates.add(coordinates);
			
		} else {
			int highestDensity = get(highestDensityCoordinates.get(0));
			
			if (density > highestDensity) {
				highestDensityCoordinates.clear();
				highestDensityCoordinates.add(coordinates);
				
			} else if (density == highestDensity) {
				highestDensityCoordinates.add(coordinates);
			}
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder densityMap = new StringBuilder();
		densityMap.append("\n");
		
		String highestDensity = Integer.toString(get(highestDensityCoordinates.get(0)));
		
		for (int i = 0; i < boardSize; i++) {
			
			densityMap.append("\n");
			
			for (int j = 0; j < boardSize; j++) {
				Coordinates c = new Coordinates(j, i);
				
				String density = Integer.toString(get(c));
				StringBuilder padding = new StringBuilder();
				
				while(padding.length() + density.length() < highestDensity.length()) {
					padding.append(" ");
				}
				
				densityMap.append(highestDensity.equals(density) ? "[" : " ");
				densityMap.append(padding);
				densityMap.append(density);
				densityMap.append(highestDensity.equals(density) ? "]" : " ");
				
			}
			densityMap.append("\n");
		}
		return densityMap.toString();
	}
	
	public List<Coordinates> getHighestDensityCoordinates() {
		return highestDensityCoordinates;
	}
	

}
