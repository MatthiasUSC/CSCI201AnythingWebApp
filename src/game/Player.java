package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import image.ImageHistory;
import image.TextUtil.AlignmentX;
import image.TextUtil.AlignmentY;

public class Player {
    private static final long JUDGE_TIMEOUT = 60L*1000L;
    
    final Room r;
    final byte id;
    BufferedImage img = null;
    Player(final Room r,final byte id) {this.r = r; this.id = id;}
    
    ImageHistory round = null;
    
    public BufferedImage updateImage(final AlignmentY y,final AlignmentX x,final int size,final double outlineSize,
                                     final Color fill,final Color outline,final Color bgH,final Color bgF,final Font f,
                                     final int style,final char...text) {
        round.pushState(y,x,size,outlineSize,fill,outline,bgH,bgF,f,style,text);
        return round.getImage();
    }
    
    void lobby() throws InterruptedException {
        if(id == 0) { // host starts game
            //TODO wait for host to start game
            r.run();
        }
        synchronized(r) {while(r.getState() == State.LOBBY) r.wait();}
    }
    
    void editImage() throws InterruptedException {
        round = new ImageHistory(r.images[r.roundImages[r.round]]);
        final Thread t = new Thread() {
            @Override
            public void run() {
                while(r.getState() == game.State.START) {
                    
                }
            }
        };
        t.start();
        t.join(r.timeLimit);
        if(t.isAlive()) {t.interrupt(); t.join();}
        synchronized(this) {
            img = round.getImage();
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

































