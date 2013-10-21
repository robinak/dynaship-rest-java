package se.dynabyte.dynaship.service.getmove.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CoordinatesTest {
	
	@Test
	public void compareTo_returns_zero_when_same_coordinate_values() {
		Coordinates a = new Coordinates(0, 1);
		Coordinates b = new Coordinates(0, 1);
		
		assertEquals(0, a.compareTo(b));
	}
	
	@Test
	public void compareTo_returns_minus_one_when_compared_to_has_higher_y_value() {
		Coordinates a = new Coordinates(1, 1);
		Coordinates b = new Coordinates(1, 2);
		
		assertEquals(-1, a.compareTo(b));
	}
	
	@Test
	public void compareTo_returns_minus_one_when_compared_to_has_higher_x_value_and_equal_y_value() {
		Coordinates a = new Coordinates(1, 1);
		Coordinates b = new Coordinates(2, 1);
		
		assertEquals(-1, a.compareTo(b));
	}
	
	@Test
	public void compareTo_returns_one_when_compared_to_has_lower_y_value() {
		Coordinates a = new Coordinates(1, 1);
		Coordinates b = new Coordinates(1, 0);
		
		assertEquals(1, a.compareTo(b));
	}
	
	@Test
	public void compareTo_returns_one_when_compared_to_has_lower_x_value_and_equal_y_value() {
		Coordinates a = new Coordinates(1, 1);
		Coordinates b = new Coordinates(0, 1);
		
		assertEquals(1, a.compareTo(b));
	}
	
	@Test
	public void sort() {
		List<Coordinates> coordinates = new ArrayList<Coordinates>();
		Coordinates a = new Coordinates(0, 0);
		Coordinates b = new Coordinates(1, 0);
		Coordinates c = new Coordinates(2, 0);
		Coordinates d = new Coordinates(1, 1);
		Coordinates e = new Coordinates(2, 1);
		Coordinates f = new Coordinates(3, 1);
		
		coordinates.addAll(Arrays.asList(f, a, e, b, d, c));
		
		Collections.sort(coordinates);
		
		assertSame(a, coordinates.get(0));
		assertSame(b, coordinates.get(1));
		assertSame(c, coordinates.get(2));
		assertSame(d, coordinates.get(3));
		assertSame(e, coordinates.get(4));
		assertSame(f, coordinates.get(5));
		
	}

}
