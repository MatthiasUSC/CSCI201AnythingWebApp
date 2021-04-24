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
    public BufferedImage img = null;
    Player(final Room r,final byte id) {this.r = r; this.id = id;}
    
    ImageHistory round = null;
    
    public BufferedImage updateImage(final AlignmentY y,final AlignmentX x,final int size,final double outlineSize,
                                     final Color fill,final Color outline,final Color bgH,final Color bgF,final Font f,
                                     final int style,final char...text) {
        round.pushState(y,x,size,outlineSize,fill,outline,bgH,bgF,f,style,text);
        return round.getImage();
    }
    
    byte judge() throws InterruptedException {
        // If judge doesn't choose, all players will get score.
        byte selection = r.judge;
        //TODO get selection
        return selection;
    }
}

































