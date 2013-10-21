package se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit;

import java.util.Collection;
import java.util.Comparator;

public class LargestFirstComparator implements Comparator<Collection<?>> {

	@Override
	public int compare(Collection<?> c1, Collection<?> c2) {
		
		if (c1.size() < c2.size()) {
			return 1;
			
		} else if (c1.size() > c2.size()) {
			return -1;
		}
		return 0;
	}

}
