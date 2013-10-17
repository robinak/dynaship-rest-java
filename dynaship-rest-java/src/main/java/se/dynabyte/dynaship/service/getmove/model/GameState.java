package se.dynabyte.dynaship.service.getmove.model;

import java.util.Collection;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameState {
	
	private final int boardSize;
	private final Collection<Shot> shots;
	private final Collection<Ship> ships;

	@JsonCreator
	public GameState(
			@JsonProperty("size") int boardSize,
			@JsonProperty("shots") Collection<Shot> shots,
			@JsonProperty("boats") Collection<Ship> ships) {
		
		this.boardSize = boardSize;
		this.shots = shots;
		this.ships = ships;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public Collection<Shot> getShots() {
		return shots;
	}

	public Collection<Ship> getShips() {
		return ships;
	}
	
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).build();
	}
}
