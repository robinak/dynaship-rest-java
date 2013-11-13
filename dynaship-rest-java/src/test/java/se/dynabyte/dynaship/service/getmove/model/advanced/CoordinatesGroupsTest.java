package se.dynabyte.dynaship.service.getmove.model.advanced;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroup.Direction;
import se.dynabyte.dynaship.service.getmove.model.advanced.CoordinatesGroups;

public class CoordinatesGroupsTest {
	
	private Coordinates x1y0;
	private Coordinates x2y0;
	private Coordinates x3y0;
	private Coordinates x4y0;
	
	private Coordinates x0y1;
	private Coordinates x0y2;
	private Coordinates x0y3;
	
	private CoordinatesGroup h1;
	private CoordinatesGroup h2;
	private CoordinatesGroup h1h2;
	
	private CoordinatesGroup h3;
	private CoordinatesGroup h2h3;
	
	private CoordinatesGroup h4;
	private CoordinatesGroup h3h4;
	
	private CoordinatesGroup h1h2h3;
	private CoordinatesGroup h1h2h3h4;
	
	private CoordinatesGroup v1;
	private CoordinatesGroup v2;
	private CoordinatesGroup v3;
	
	private CoordinatesGroup v1v2v3;
	
	@Before
	public void setup() {
		
		x1y0 = new Coordinates(1, 0);
		x2y0 = new Coordinates(2, 0);
		x3y0 = new Coordinates(3, 0);
		x4y0 = new Coordinates(4, 0);
		
		x0y1 = new Coordinates(0, 1);
		x0y2 = new Coordinates(0, 2);
		x0y3 = new Coordinates(0, 3);
		
		h1 = new CoordinatesGroup(Direction.HORIZONTAL);
		h1.add(x1y0);
		
		h2 = new CoordinatesGroup(Direction.HORIZONTAL);
		h2.add(x2y0);
		
		h1h2 = new CoordinatesGroup(Direction.HORIZONTAL);
		h1h2.add(x1y0);
		h1h2.add(x2y0);
		
		h3 = new CoordinatesGroup(Direction.HORIZONTAL);
		h3.add(x3y0);
		
		h2h3 = new CoordinatesGroup(Direction.HORIZONTAL);
		h2h3.add(x2y0);
		h2h3.add(x3y0);
		
		h4 = new CoordinatesGroup(Direction.HORIZONTAL);
		h4.add(x4y0);
		
		h3h4 = new CoordinatesGroup(Direction.HORIZONTAL);
		h3h4.add(x3y0);
		h3h4.add(x4y0);
		
		h1h2h3 = new CoordinatesGroup(Direction.HORIZONTAL);
		h1h2h3.add(x1y0);
		h1h2h3.add(x2y0);
		h1h2h3.add(x3y0);
		
		h1h2h3h4 = new CoordinatesGroup(Direction.HORIZONTAL);
		h1h2h3h4.add(x1y0);
		h1h2h3h4.add(x2y0);
		h1h2h3h4.add(x3y0);
		h1h2h3h4.add(x4y0);
		
		v1 = new CoordinatesGroup(Direction.VERTICAL);
		v1.add(x0y1);
		
		v2 = new CoordinatesGroup(Direction.VERTICAL);
		v2.add(x0y2);
		
		v3 = new CoordinatesGroup(Direction.VERTICAL);
		v3.add(x0y3);
		
		v1v2v3 = new CoordinatesGroup(Direction.VERTICAL);
		v1v2v3.add(x0y1);
		v1v2v3.add(x0y2);
		v1v2v3.add(x0y3);
	}
	
	@Test
	public void mergeAdjacentGroups_merges_h1_and_h2_to_h3() {
		CoordinatesGroups mergedGroups = buildGroups(h1, h2);
		mergedGroups.mergeAdjacent();
		
		assertEquals(1, mergedGroups.size());
		assertTrue(mergedGroups.contains(h1h2));
	}
	
	@Test
	public void mergeAdjacentGroups_merges_h1_and_h1h2_to_h1h2() {
		CoordinatesGroups mergedGroups = buildGroups(h1, h1h2);
		mergedGroups.mergeAdjacent();
		
		assertEquals(1, mergedGroups.size());
		assertTrue(mergedGroups.contains(h1h2));
	}
	
	@Test 
	public void mergeAdjacentGroups_merges_h1_and_h2_and_h1h2_and_h3_and_h4_and_h3h4_to_h1h2h3h4() {
		CoordinatesGroups mergedGroups = buildGroups(h1, h2, h1h2, h3, h4, h3h4);
		mergedGroups.mergeAdjacent();
		
		assertEquals(1, mergedGroups.size());
		assertTrue(mergedGroups.contains(h1h2h3h4));
	}
	
	@Test 
	public void mergeAdjacentGroups_merges_h1_and_h3_and_h2_to_h1h2h3() {
		CoordinatesGroups mergedGroups = buildGroups(h1, h3, h2 );
		mergedGroups.mergeAdjacent();
		
		assertEquals(1, mergedGroups.size());
		assertTrue(mergedGroups.contains(h1h2h3));
	}
	
	@Test 
	public void mergeAdjacentGroups_merges_h1h2_and_h2h3_and_v2_and_v1_and_v3_to_h1h2h3_and_v1v2v3() {
		CoordinatesGroups mergedGroups = buildGroups(h1h2, h2h3, v2, v1, v3);
		mergedGroups.mergeAdjacent();
		
		assertEquals(2, mergedGroups.size());
		assertTrue(mergedGroups.contains(h1h2h3));
		assertTrue(mergedGroups.contains(v1v2v3));
	}
	
	@Test
	public void mergeAdjacentGroups_does_not_merge_h1_and_v1() {
		CoordinatesGroups mergedGroups = buildGroups(h1, v1);
		mergedGroups.mergeAdjacent();
		
		assertEquals(2, mergedGroups.size());
		assertTrue(mergedGroups.contains(h1));
		assertTrue(mergedGroups.contains(v1));
	}
	
	private CoordinatesGroups buildGroups(CoordinatesGroup... coordinatesGroups) {
		CoordinatesGroups groups = new CoordinatesGroups();
		
		for(CoordinatesGroup group : coordinatesGroups) {
			groups.add(group);
		}
		
		return groups;
	}

}
