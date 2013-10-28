package se.dynabyte.dynaship.service.getmove.util.advanced;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.Shot;

public class CoordinatesUtilTest {
	
	private CoordinatesUtil coordinatesUtil = new CoordinatesUtil();
	
	@Test
	public void getCoordinates_returns_empty_collection_when_shots_are_empty() {
		Collection<Shot> emptyShots = Collections.emptyList();
		
		Collection<Coordinates> result = coordinatesUtil.getCoordinates(emptyShots);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void getCoordinates_returns_coordinates_one_one_and_two_four_when_shots_have_the_same_coordinates() {
		Coordinates one_one = new Coordinates(1, 1);
		Coordinates two_four = new Coordinates(2, 4);
		
		Collection<Shot> shots = new ArrayList<Shot>();
		shots.add(getShotForCoordinates(one_one));
		shots.add(getShotForCoordinates(two_four));
		
		Collection<Coordinates> expected = Arrays.asList(one_one, two_four);
		Collection<Coordinates> result = coordinatesUtil.getCoordinates(shots);
		assertEquals(expected, result);
	}

	private Shot getShotForCoordinates(Coordinates coordinates) {
		return new Shot(coordinates, null, 0);
	}
}
