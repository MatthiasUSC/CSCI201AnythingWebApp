package contextAttributes;
import java.util.HashMap;
import java.util.Map;

import game.Room;

public class GameManager {
	
	public class GameNotFoundException extends RuntimeException {}
	
	private final Map<Long, Room> games;
	private long nextGameCode;
	
	public GameManager() {
		games = new HashMap<Long, Room>();
		nextGameCode = 0;
	}
	
	public long addGame(Room room) {
		long gameCode = nextGameCode;
		nextGameCode++;
		games.put(gameCode, room);
		return gameCode;
	}
	
	public Room getGame(long gameCode) throws GameNotFoundException{
		Room game =  games.get(gameCode);
		if(game == null) {
			throw new GameNotFoundException();
		} else {
			return game;
		}
	}
}
