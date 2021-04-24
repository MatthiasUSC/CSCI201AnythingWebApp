package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import image.ImageHistory;
import image.TextUtil.AlignmentX;
import image.TextUtil.AlignmentY;

public class Player {
    final Room r;
    final byte id;
    final String name;
    public BufferedImage img = null;
    public final BlockingQueue<GameState> eventQ = new LinkedBlockingQueue<GameState>(); 
    Player(final Room r,final byte id,final String name) {
        this.r = r;
        this.id = id;
        this.name = name;
    }
    
    ImageHistory round = null;
    
    public BufferedImage updateImage(final AlignmentY y,final AlignmentX x,final int size,final double outlineSize,
                                     final Color fill,final Color outline,final Color bgH,final Color bgF,final Font f,
                                     final int style,final char...text) {
        round.pushState(y,x,size,outlineSize,fill,outline,bgH,bgF,f,style,text);
        return round.getImage();
    }
    
    private static StringBuilder wrap(final String s) {return new StringBuilder("\"").append(s).append('"');}
    private static StringBuilder wrap(final String k,final CharSequence v) {return wrap(k).append(':').append(v);}
    @Override
    public String toString() {
        return new StringJoiner(",","{","}").add(wrap("id",Integer.toString(id)))
                                            .add(wrap("name",wrap(name)))
                                            .toString();
    }
}

































