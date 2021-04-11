package data;
import java.util.ArrayList;





public class Game {
	public class AddPlayerException extends RuntimeException {}
	
	enum State {
		LOBBY,
		PLAYING
	}
	
	private State gameState;
	private ArrayList<Player> players;
	private int maxAmountPlayers;
	
	public Game() {
		gameState = State.LOBBY;
		players = new ArrayList<Player>();
		maxAmountPlayers = 10;
	}
	
	public void addPlayer(Player player) throws AddPlayerException {
		players.add(player);
	}
	
	//TODO should make this clone players, or something maybe more efficient to prevent break in encapsulation
	public ArrayList<Player> getAllPlayers(){
		return players;
	}
}
