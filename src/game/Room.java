package game;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import util.container.BitSet;

public class Room extends Thread {
    private static final short MAX_CODE = 10000;
    private static short CODE = 0;
    private static final BitSet ALLOCATED = new BitSet(MAX_CODE);
    private static final Map<Short,Room> LOBBIES = new HashMap<>();
    private static final Map<UUID,Room> REGISTRY = new HashMap<>();
    
    byte add = 0,ready = 0,round = 0,judge = 0,winner = -1;
    final BufferedImage[] images;
    final byte[] roundImages;
    public BufferedImage[] finished = null;
    public byte[] scramble = null,unscramble = null;
    public final Player[] players;
    final short code;
    final byte rounds;
    final long timeLimit;
    Thread lobby;
    GameState state = GameState.LOBBY;
    
    public static synchronized Room createRoom(final byte size,final long timeLimit,final byte rounds,
                                               final BufferedImage[] images) {
        final Room r = new Room(size,timeLimit,rounds,images);
        LOBBIES.put(r.code,r);
        return r;
    }
    private Room(final byte size,final long timeLimit,final byte rounds,final BufferedImage[] images) {
        code = ALLOCATED.setNextFalse(CODE);
        if(++CODE == MAX_CODE) CODE = 0;
        roundImages = new byte[rounds];
        for(byte i = 0;i < rounds;++i) roundImages[i] = i;
        {
            final ThreadLocalRandom r = ThreadLocalRandom.current();
            for(byte i = (byte)(rounds - 1);i > 0;--i) {
                final byte t;
                {
                    final byte j = (byte)(r.nextInt() % (i + 1));
                    t = roundImages[j];
                    roundImages[j] = roundImages[i];
                }
                roundImages[i] = t;
            }
        }
        players = new Player[size];
        this.timeLimit = timeLimit;
        this.images = images;
        this.rounds = rounds;
    }
    
    public static synchronized Room getLobby(final short code) {return LOBBIES.get(code);}
    
    public synchronized Player addPlayer() throws InterruptedException {
    	
    	if(state == GameState.LOBBY && add < players.length){
    		Player newPlayer = new Player(this,add);
    		players[add++] = newPlayer;
    		
    		/* EVENT
             * JOIN
             * Send player list to all players in lobby
             */
    		broadcastEvent(GameState.LOBBY);
    		return newPlayer;
    	} else {
    		return null;
    	}
        
    }
    
    public void run() {
        {
            final UUID id = UUID.randomUUID();
            REGISTRY.put(id,this);
        }
        LOBBIES.remove(code);
        ALLOCATED.clear(code);
        try {
            while(++round != rounds) {
                onStart();
                onJudge();
            }
        } catch(final InterruptedException e) {
            // TODO: handle exception
        }
        onEnd();
    }
    
    private void onStart() throws InterruptedException {
        state = GameState.START;
        
        /* EVENT
         * START
         * Send selected image to all players
         */
        broadcastEvent(GameState.START);
        
        //TODO long poll stuff while time limit
        TimeUnit.MILLISECONDS.sleep(timeLimit);
        state = GameState.JUDGE;
        
        // wait for finish
        finished = new BufferedImage[scramble.length];
        for(final Player p : players) if(p.id != judge) finished[scramble[p.id]] = p.img;
        
        
        /* EVENT
         * TIMEOUT
         * Send all images to all players
         */
        broadcastEvent(GameState.TIMEOUT);
    }
    
    private void onJudge() throws InterruptedException {
        scramble = new byte[players.length - 1];
        for(byte i = 0;i < judge;++i) scramble[i] = i;
        for(byte i = judge;i < players.length - 1;scramble[i] = ++i);
        {
            final ThreadLocalRandom r = ThreadLocalRandom.current();
            for(byte i = (byte)(scramble.length - 1);i > 0;--i) {
                final byte t;
                {
                    final byte j = (byte)(r.nextInt() % (i + 1));
                    t = scramble[j];
                    scramble[j] = scramble[i];
                }
                scramble[i] = t;
            }
        }
        for(byte i = 0;i < scramble.length;++i) unscramble[scramble[i]] = scramble[i] > judge? (byte)(i - 1) : i;
        //TODO put winner = players[judge].judge(); somewhere to select the judge; probs somewhere in long poll
        
        /* EVENT
         * JUDGE
         * Sends judged image to all players, also maybe each players points to update?
         */
         broadcastEvent(GameState.JUDGE);
    }
    
    public byte getWinner() {return winner;}
    
    private void onEnd() {
        state = GameState.END;
        
        /* EVENT
         * END
         * Sends who has the most points
         */
        broadcastEvent(GameState.END);
    }
    
    public BufferedImage[] getImages() {
    	return images;
    }
    
    public synchronized GameState getGameState() {return state;}
    
    public BufferedImage getRoundImage() {return images[roundImages[round]];}
    
    /*
     * Sends an event to all connected players
     */
    private void broadcastEvent(GameState event) {
    	for(Player p : players) {
    		p.sendEvent(event);
    	}
    }
}


































