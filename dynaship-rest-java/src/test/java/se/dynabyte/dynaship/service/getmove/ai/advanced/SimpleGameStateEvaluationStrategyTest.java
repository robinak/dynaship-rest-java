package se.dynabyte.dynaship.service.getmove.ai.advanced;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;

import se.dynabyte.dynaship.service.getmove.model.Coordinates;
import se.dynabyte.dynaship.service.getmove.model.GameState;
import se.dynabyte.dynaship.service.getmove.model.Shot;
import se.dynabyte.dynaship.service.getmove.util.advanced.CoordinatesUtil;
import se.dynabyte.dynaship.service.getmove.util.advanced.Randomizer;
import se.dynabyte.dynaship.service.getmove.util.advanced.ShipsUtil;

public class SimpleGameStateEvaluationStrategyTest {
	
	private SimpleGameStateEvaluationStrategy strategy;
	
	@Mock private ShipsUtil shipsUtil;
	@Mock private CoordinatesUtil coordinatesUtil;
	@Mock private GameState gameState;
	@Mock private Randomizer randomUtil;
	
	private List<Coordinates> allCoordinates;
	
	@Before
	public void setup() {
		initMocks(this);
		strategy = new SimpleGameStateEvaluationStrategy(shipsUtil, coordinatesUtil, randomUtil);
		
		allCoordinates = new ArrayList<Coordinates>();
		allCoordinates.add(new Coordinates(0, 0));
		allCoordinates.add(new Coordinates(1, 0));
		allCoordinates.add(new Coordinates(0, 1));
		allCoordinates.add(new Coordinates(1, 1));
	}
	
	@Test
	public void getMove_returns_null_when_all_coordinates_equals_existing_shot_coordinates() {
		
		when(coordinatesUtil.getAllCoordinates(anyInt())).thenReturn(allCoordinates);
		when(coordinatesUtil.getCoordinates(Matchers.<Collection<Shot>>any())).thenReturn(allCoordinates);
		
		Coordinates result = strategy.getMove(gameState);
		assertNull(result);
	}
	
	@Test
	public void getMove_returns_coordinates_zero_one_when_no_shots_and_random_index_is_two() {
		
		when(coordinatesUtil.hasEnoughUnexploredOrSeaworthyNeighboursToFitSmallestSeaworthyShip(
				any(Coordinates.class), 
				anyInt(), 
				anyInt(), 
				anyCollectionOf(Coordinates.class), 
				anyCollectionOf(Coordinates.class))
				).thenReturn(true);
		
		when(coordinatesUtil.getAllCoordinates(anyInt())).thenReturn(allCoordinates);
		when(coordinatesUtil.getCoordinates(anyCollectionOf(Shot.class))).thenReturn(Collections.<Coordinates>emptyList());
		when(randomUtil.getRandomInt(anyInt())).thenReturn(2);
		
		Coordinates zero_one = new Coordinates(0, 1);
		Coordinates result = strategy.getMove(gameState);
		
		assertEquals(zero_one, result);
	}
	
	@Test
	public void getMove_returns_coordinates_one_one_when_all_other_coordinates_are_shot_coordinates() {
		
		when(coordinatesUtil.hasEnoughUnexploredOrSeaworthyNeighboursToFitSmallestSeaworthyShip(
				any(Coordinates.class), 
				anyInt(), 
				anyInt(), 
				anyCollectionOf(Coordinates.class), 
				anyCollectionOf(Coordinates.class))
				).thenReturn(true);
		
		Coordinates one_one = new Coordinates(1, 1);
		List<Coordinates> shotCoordinates = new ArrayList<Coordinates>(allCoordinates);
		shotCoordinates.remove(one_one);
		
		when(coordinatesUtil.getAllCoordinates(anyInt())).thenReturn(allCoordinates);
		when(coordinatesUtil.getCoordinates(Matchers.<Collection<Shot>>any())).thenReturn(shotCoordinates);
		when(randomUtil.getRandomInt(anyInt())).thenReturn(0);
		
		
		Coordinates result = strategy.getMove(gameState);
		assertEquals(one_one, result);
	}

}
