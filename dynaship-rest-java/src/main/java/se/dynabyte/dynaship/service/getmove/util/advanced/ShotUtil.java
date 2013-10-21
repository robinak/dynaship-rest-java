package se.dynabyte.dynaship.service.getmove.util.advanced;

import java.util.ArrayList;
import java.util.Collection;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.Shot;

public class ShotUtil {
	
	public static Collection<Coordinates> getCoordinates(Collection<Shot> shots) {
		Collection<Coordinates> shotCoordinates = new ArrayList<Coordinates>();
		
		for (Shot shot : shots) {
			shotCoordinates.add(shot.getCoordinates());
		}
		
		return shotCoordinates;
	}

}
