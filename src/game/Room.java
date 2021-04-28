package game;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import util.container.BitSet;

public class Room {
    //private static final long JUDGE_TIMEOUT = 60L;
    private static final short ROUND_BUFFER = 5;
    private static final short MAX_CODE = 10000;
    private static short CODE = 0;
    private static final BitSet ALLOCATED = new BitSet(MAX_CODE);
    private static final Map<Short,Room> LOBBIES = new HashMap<>();
    //private static final Map<UUID,Room> REGISTRY = new HashMap<>(); //TODO do we need this?
    
    final UUID uuid = UUID.randomUUID();
    private byte add = 0,round = 0,judge = 0;
    public byte winner = -1;
    private final BufferedImage[] images;
    private final byte[] roundImages;
    public BufferedImage[] finished = null;
    public byte[] scramble = null,unscramble = null;
    public final Player[] players;
    public final short code;
    private final byte rounds;
    private final short timeLimit;
    private GameState state = GameState.JOIN;
    
    public static synchronized Room createRoom(final byte size,final short timeLimit,final byte rounds,
                                               final BufferedImage[] images) {
        final Room r = new Room(size,timeLimit,rounds,images);
        LOBBIES.put(r.code,r);
        return r;
    }
    private Room(final byte size,final short timeLimit,final byte rounds,final BufferedImage[] images) {
        code = ALLOCATED.setNextFalse(CODE);
        if(++CODE == MAX_CODE) CODE = 0;
        roundImages = new byte[rounds];
        for(byte i = 0;i < rounds;++i) roundImages[i] = i % images.length;
        {
            final ThreadLocalRandom r = ThreadLocalRandom.current();
            for(byte i = (byte)(rounds - 1);i > 0;--i) {
                final byte t;
                {
                    final byte j = (byte)r.nextInt(i + 1);
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
    
    public synchronized Player addPlayer(final String name) throws InterruptedException {
        if(state == GameState.JOIN && add < players.length) {
            final Player p = players[add] = new Player(this,add++,name);
            broadcast(GameState.JOIN);
            return p;
        }
        return null;
    }
    
    private void broadcast(final GameState s) {
        state = s;
        for(byte p = 0;p < add;++p) players[p].eventQ.add(state);
    }
    
    public void start() throws InterruptedException {
        //REGISTRY.put(uuid,this);
        LOBBIES.remove(code);
        ALLOCATED.clear(code);
        run();
    }
    
    private void run() throws InterruptedException {
        broadcast(GameState.START);
        
        TimeUnit.SECONDS.sleep(timeLimit);
        
        // round over
        scramble = new byte[add - 1];
        unscramble = new byte[add];
        for(byte i = 0;i < judge;++i) scramble[i] = i;
        for(byte i = judge;i < add - 1;scramble[i] = ++i);
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
        // get finished images
        finished = new BufferedImage[scramble.length];
        for(byte p = 0;p < add;++p) if(p != judge) finished[scramble[p]] = players[p].img;
        broadcast(GameState.TIMEOUT);
    }
    
    public void setWinner(final byte b) throws InterruptedException {
        winner = unscramble[b];
        broadcast(GameState.JUDGE);
        if(++round == rounds) {
            ++judge;
            broadcast(GameState.END);
            //REGISTRY.remove(uuid);
        } else {
            TimeUnit.SECONDS.sleep(ROUND_BUFFER);
            run();
        }
    }
    byte getJudge() {return judge;}
    public BufferedImage getRoundImage() {return images[roundImages[round]];}
    
    public String playerJSON() {
        final StringJoiner o = new StringJoiner("\",\"","\"players\":[\"","\"]");
        for(byte p = 0;p < add;++p) o.add(players[p].name);
        return o.toString();
    }
}
