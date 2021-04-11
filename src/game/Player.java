package game;

import java.awt.image.BufferedImage;

import image.ImageHistory;

public class Player {
    private static final long JUDGE_TIMEOUT = 60L*1000L;
    
    final Room r;
    final byte id;
    BufferedImage img = null;
    Player(final Room r,final byte id) {this.r = r; this.id = id;}
    
    void lobby() throws InterruptedException {
        if(id == 0) { // host starts game
            //TODO wait for host to start game
            r.run();
        }
        synchronized(r) {while(r.state == State.LOBBY) r.wait();}
    }
    
    void editImage() throws InterruptedException {
        final ImageHistory ih = new ImageHistory(r.images[r.roundImages[r.round]]);
        final Thread t = new Thread() {
            @Override
            public void run() {
                //TODO thread stuff here.
            }
        };
        t.start();
        t.join(r.timeLimit);
        if(t.isAlive()) {t.interrupt(); t.join();}
        synchronized(this) {
            img = ih.getImage();
            notify();
        }
    }
    
    void displayImages(final byte[] scramble) {
        for(final Player p : r.players) {
            if(p != Player.this) {
                // TODO display p.img
                p.img = null;
            }
        }
    }
    
    void waitForJudge(final byte[] scramble) throws InterruptedException {
        displayImages(scramble);
        synchronized(r) {while(r.winner == -1) r.wait();}
        //TODO highlight winner and image
    }
    
    byte judge(final byte[] scramble) throws InterruptedException {
        displayImages(scramble);
        // If judge doesn't choose, all players will get score.
        byte selection = r.judge;
        final Thread t = new Thread() {
            @Override
            public void run() {
                //TODO get selection
            }
        };
        t.start();
        t.join(JUDGE_TIMEOUT);
        if(t.isAlive()) {t.interrupt(); t.join();}
        return selection;
    }
}

































