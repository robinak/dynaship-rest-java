package se.dynabyte.dynaship.service.getmove.model.advanced.probabilitydensity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroups;

public class DensityMap extends HashMap<Coordinates, Integer> {

	private static final long serialVersionUID = 1L;
	
	private final int boardSize;
	private final Collection<Coordinates> seaworthyCoordinates;
	
	public DensityMap(int boardSize, Collection<Coordinates> seaworthyCoordinates) {
		
		for(int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				put(new Coordinates(j, i), 0);
			}
		}
		this.boardSize = boardSize;
		this.seaworthyCoordinates = seaworthyCoordinates;
	}
	
	public void incrementFor(CoordinatesGroups coordinatesGroups) {
		for (CoordinatesGroup group : coordinatesGroups) {
			
			int seaworthyBonus = 0;
			for ( Coordinates seaworthy : seaworthyCoordinates) {
				if (group.contains(seaworthy)) {
					seaworthyBonus = seaworthyBonus + boardSize * boardSize;
				}
			}
			
			for (Coordinates coordinates : group) {
				int density = get(coordinates);
				put(coordinates, ++density + seaworthyBonus);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder densityMap = new StringBuilder();
		densityMap.append("\n");
		
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				Coordinates c = new Coordinates(j, i);
				
				int density = get(c);
				
				if (density < 1000) {
					densityMap.append("0");
					
					if (density < 100) {
						densityMap.append("0");
						
						if (density < 10) {
							densityMap.append("0");
						}
					}
				}
				
				densityMap.append(get(c));
				densityMap.append(" ");
				
			}
			densityMap.append("\n");
		}
		return densityMap.toString();
	}
	
	public List<Coordinates> getCoordinatesForHighestDensity() {
		
		int highestDensity = Integer.MIN_VALUE;
		List<Coordinates> highestDensityCoordinates = new ArrayList<Coordinates>();
		
		for (Coordinates coordinates : keySet()) {
			
			int density = get(coordinates);
			if (density > highestDensity) {
				highestDensity = density;
				highestDensityCoordinates.clear();
				highestDensityCoordinates.add(coordinates);
				
			} else if (density == highestDensity) {
				highestDensityCoordinates.add(coordinates);
			}
		}

		return highestDensityCoordinates;
	}
	

}
