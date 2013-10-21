package se.dynabyte.dynaship.service.getmove.ai.advanced.existinghit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.model.State;

public class ShotCollector {
	
	private final Map<State, Collection<Shot>> shotsByState;
	private final Collection<Shot> hits;
	private final Collection<Shot> misses;
	private final Collection<Shot> capsized;
	
	public ShotCollector(Collection<Shot> shots) {
		hits = new ArrayList<Shot>();
		misses = new ArrayList<Shot>();
		capsized = new ArrayList<Shot>();
		
		for (Shot shot : shots) {
			switch(shot.getState()) {
			case SEAWORTHY: hits.add(shot); 	break;
			case MISSED:	misses.add(shot); 	break;
			case CAPSIZED: 	capsized.add(shot); break;
			}
		}
		
		shotsByState = new HashMap<State, Collection<Shot>>();
		shotsByState.put(State.SEAWORTHY, hits);
		shotsByState.put(State.MISSED, misses);
		shotsByState.put(State.CAPSIZED, capsized);
	}
	
	public Collection<Shot> collect(State... states) {
		Collection<Shot> collected = new ArrayList<Shot>();
		
		for (State state : states) {
			Collection<Shot> shotsForState = new ArrayList<Shot>(shotsByState.get(state));
			collected.addAll(shotsForState);
		}
		
		return collected;
	}
}
