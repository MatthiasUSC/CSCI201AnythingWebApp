package contextAttributes;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.Game;
import data.Player;



public class GameManager {
	
	public class GameNotFoundException extends RuntimeException {}
	
	
	private final Map<Long, Game> games;
	private long nextGameCode;
	
	public GameManager() {
		games = new TreeMap<Long, Game>();
		nextGameCode = 0;
	}
	
	//TODO create private API that uses Game object references, and create public API that uses gameCodes
	
	/*
	 * Creates a new empty game, returns gamecode
	 */
	public long createGame() {
		long gameCode = nextGameCode;
		nextGameCode++;
		games.put(gameCode, new Game());
		return gameCode;
	}
	
	/*
	 * Creates a new game with a player, returns gamecode
	 */
	public long createGameWithPlayer(Player player) throws GameNotFoundException, Game.AddPlayerException {
		long gameCode = createGame();
		addPlayerToGame(gameCode, player);
		return gameCode;
	}
	
	/*
	 * Gets a game using a gamecode.
	 * Returns null if game doesnt exist
	 */
	public Game getGameFromGameCode(long gameCode) throws GameNotFoundException{
		Game game =  games.get(gameCode);
		if(game == null) {
			throw new GameNotFoundException();
		} else {
			return game;
		}
		
	}
	
	/*
	 * Adds a player to a game using gamecode
	 */
	public void addPlayerToGame(long gameCode, Player player) throws GameNotFoundException, Game.AddPlayerException {
		Game game = getGameFromGameCode(gameCode);
		game.addPlayer(player);
	}
	
	public ArrayList<Player> getPlayersInGame(long gameCode) throws GameNotFoundException {
		//TODO might want to make copies of objects when passing them out to prevent a break in encapsulation
		// As any code that calls this function will be able to call Player public methods.
		Game game = getGameFromGameCode(gameCode);
		return game.getAllPlayers();
	}
}
