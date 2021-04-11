package game;

import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import util.container.BitSet;

public class Room {
    private static final short MAX_CODE = 10000;
    private static short CODE = 0;
    private static final BitSet ALLOCATED = new BitSet(MAX_CODE);
    
    byte add = 0,ready = 0,round = 0,judge = 0,winner = -1;
    final BufferedImage[] images;
    final byte[] roundImages;
    final Player[] players;
    final short code;
    final byte rounds;
    final long timeLimit;
    Thread lobby;
    State state = State.LOBBY;
    
    public static synchronized Room createRoom(final byte size,final long timeLimit,final byte rounds,
                                               final BufferedImage[] images) {
        return new Room(size,timeLimit,rounds,images);
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
    
    public synchronized Player addPlayer() throws InterruptedException {
        if(state == State.LOBBY && add < players.length) {
            players[add] = new Player(this,add);
            if(add == 0) {
                final Player host = players[add];
                lobby = new Thread() {
                    @Override
                    public void run() {
                        try {host.lobby();}
                        catch(final InterruptedException e) {}
                    }
                };
                lobby.start();
            }
            return players[add++];
        }
        return null;
    }
    
    void run() throws InterruptedException {
        synchronized(this) {
            state = State.START;
            notifyAll();
        }
        while(++round != rounds) {
            onStart();
            onJudge();
        }
        onEnd();
    }
    
    private void onStart() throws InterruptedException {
        for(final Player p : players) if(p.id != judge) synchronized(p) {if(p.img != null) p.wait();}
        state = State.JUDGE;
    }
    
    private void onJudge() throws InterruptedException {
        final byte[] scramble = new byte[players.length - 1];
        for(byte i = 0;i < judge;++i) scramble[i] = i;
        for(byte i = judge;i < players.length - 1;scramble[i] = ++i);
        for(final Player p : players) if(p.id != judge) p.waitForJudge(scramble);
        synchronized(this) {
            winner = players[judge].judge(scramble);
            notifyAll();
        }
    }
    
    private void onEnd() {
        state = State.END;
    }
}


































