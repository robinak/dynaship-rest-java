package se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LargestFirstComparatorTest {
	
	private List<Collection<Object>> objectCollections;
	private Collection<Object> sizeOne;
	private Collection<Object> sizeTwo;
	private Collection<Object> sizeThree;
	
	@Before
	public void setup() {
		sizeOne = Arrays.asList(new Object());
		sizeTwo = Arrays.asList(new Object(), new Object());
		sizeThree = Arrays.asList(new Object(), new Object(), new Object());
		
		objectCollections = new ArrayList<Collection<Object>>();
		objectCollections.add(sizeOne);
		objectCollections.add(sizeTwo);
		objectCollections.add(sizeThree);
	}
	
	@Test
	public void sortLargestCollectionFirst() {
		
		Collections.sort(objectCollections, new LargestFirstComparator());
		
		Iterator<Collection<Object>> it = objectCollections.iterator();
		assertEquals(3, it.next().size());
		assertEquals(2, it.next().size());
		assertEquals(1, it.next().size());
	}

}
