package se.dynabyte.dynaship.service.getmove.util.advanced;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.Shot;

public class CoordinatesUtil {
	
	public static Collection<Coordinates> getCoordinates(Collection<Shot> shots) {
		Collection<Coordinates> shotCoordinates = new ArrayList<Coordinates>();
		
		for (Shot shot : shots) {
			shotCoordinates.add(shot.getCoordinates());
		}
		
		return shotCoordinates;
	}
	
	public static List<Coordinates> getAllCoordinates(int boardSize) {
		List<Coordinates> allCoordinates = new ArrayList<Coordinates>();
		
		for(int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				allCoordinates.add(new Coordinates(j, i));
			}
		}
		return allCoordinates;
	}

}
