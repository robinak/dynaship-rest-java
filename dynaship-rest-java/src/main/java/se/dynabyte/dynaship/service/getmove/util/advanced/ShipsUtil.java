package se.dynabyte.dynaship.service.getmove.util.advanced;

import java.util.Collection;

import se.dynabyte.dynaship.service.getmove.model.Ship;

public class ShipsUtil {
	
	public int getMinimumLenghtOfAliveShip(Collection<Ship> ships) {
		int minLength = Integer.MAX_VALUE;
		for (Ship ship : ships) {
			int length = ship.getLength();
 			if (length < minLength && ship.isAlive()) {
				minLength = length;
			}
		}
		return minLength;
	}

	public int getMaximumLengthOfAliveShip(Collection<Ship> ships) {
		int maxLength = Integer.MIN_VALUE;
		for (Ship ship : ships) {
			int length = ship.getLength();
 			if (length > maxLength && ship.isAlive()) {
 				maxLength = length;
			}
		}
		return maxLength;
	}

}
