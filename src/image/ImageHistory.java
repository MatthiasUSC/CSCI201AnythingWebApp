package image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import image.TextUtil.AlignmentX;
import image.TextUtil.AlignmentY;
import util.LazilyLoadedObject;
import util.container.LightweightStack;

/**
 * A class which holds an image and a traversible history of modifications.
 * 
 * @author Cole Petersen
 */
public class ImageHistory {
    /** Manages a particular frame of the image. */
    private final class State {
        /** A portion of the state at a vertical alignment. */
        private final class SubState {
            private final BufferedImage i;
            private final AlignmentX x;
            private final int w,h;
            
            private SubState(final BufferedImage i,final AlignmentX x,final int w,final int h) {
                this.i = i; this.x = x;
                this.w = w; this.h = h;
            }
            
            /** Writes the sub-state to the graphics object. */
            private final void apply(final Graphics2D g,final AlignmentY y) {
                if(i == null) return;
                final int dx = switch(x) {
                    case LEFT -> 0;
                    case CENTER -> (bw - w + 1) / 2;
                    case RIGHT -> bw - w;
                };
                final int dy = switch(y) {
                    case HEAD -> 0;
                    case TOP -> head;
                    case MID -> (bh + head + foot - h + 1) / 2;
                    case LOW -> bh + head - h;
                    case FOOT -> bh + head;
                };
                g.drawImage(i,dx,dy,w,h,null);
            }
        }
        /** A lazily-loaded substate array. */
        private final class SSLLO extends LazilyLoadedObject<SubState[]> {
            public SSLLO(final SSLLO previous) {super(previous);}
            @Override protected void init() {v = new SubState[AlignmentY.N_Y];}
            @Override
            protected void cpy() {
                init();
                System.arraycopy(p.read(),0,v,0,AlignmentY.N_Y);
            }
        }
        
        private final SSLLO parts;
        private int bgH,bgF;
        private BufferedImage cache = null;
        
        private State(final BufferedImage base) {cache = base; parts = null; bgH = bgF = Color.WHITE.getRGB();}
        private State(final State p) {parts = new SSLLO(p == null? null : p.parts); bgH = p.bgH; bgF = p.bgF;}
        
        /** Writes the text image to the state. */
        private void setSubState(final AlignmentY y,final AlignmentX x,final BufferedImage i,final int w,final int h) {
            parts.write(ss -> ss[y.ordinal()] = new SubState(i,x,w,h));
        }
        
        /** Returns the image associated with the state. */
        private BufferedImage cache() {
            if(cache == null) {
                final SubState[] ss = parts.read();
                head = ss[AlignmentY.HEAD.ordinal()] == null? 0 : ss[AlignmentY.HEAD.ordinal()].h;
                foot = ss[AlignmentY.FOOT.ordinal()] == null? 0 : ss[AlignmentY.FOOT.ordinal()].h;
                
                cache = new BufferedImage(bw,bh + head + foot,DEFAULT_TYPE);
                if(head == 0 && foot == 0) TextUtil.copyInto(base,cache);
                else TextUtil.copyInto(base,cache,head,foot,bgH,bgF);
                
                final AlignmentY[] valign = AlignmentY.values();
                final Graphics2D g = cache.createGraphics();
                for(int i = 0;i < AlignmentY.N_Y;++i) if(ss[i] != null) ss[i].apply(g,valign[i]);
                g.dispose();
            }
            return cache;
        }
    }
    
    private final LightweightStack<State> undo = new LightweightStack<>();
    private final LightweightStack<State> redo = new LightweightStack<>();
    private State current;
    private final BufferedImage base;
    private final int bw,bh;
    private int head = 0,foot = 0;
    
    private static final int DEFAULT_TYPE = BufferedImage.TYPE_INT_ARGB;
    
    public ImageHistory(final BufferedImage base) {
        bw = base.getWidth(); bh = base.getHeight();
        this.base = new BufferedImage(bw,bh,DEFAULT_TYPE);
        if(base.getType() == DEFAULT_TYPE) TextUtil.copyInto(base,this.base);
        else {
            final Graphics2D g = this.base.createGraphics();
            g.drawImage(base,0,0,null);
            g.dispose();
        }
        current = new State(this.base);
    }
    
    /** Pushes the current state to the undo stack, clears the redo stack, and applies edits. */
    public void pushState(final AlignmentY y,final AlignmentX x,final int size,final double outlineSize,
                          final Color fill,final Color outline,final Color bgH,final Color bgF,final Font f,
                          final int style,final char...text) {
        if(!redo.empty()) redo.clear();
        undo.push(current);
        current = new State(current);
        if(bgH != null) current.bgH = bgH.getRGB();
        if(bgF != null) current.bgF = bgF.getRGB();
        final int[] s = new int[] {bw,size};
        current.setSubState(y,x,text.length == 0? null : TextUtil.charsToImage(s,outlineSize,fill,outline,f,style,text),s[0],s[1]);
    }
    /** Pushes the current state to the redo stack and pops the undo stack to current state. */
    public void undo() {
        if(undo.empty()) return;
        redo.push(current);
        current = undo.pop();
    }
    /** Pushes the current state to the undo stack and pops the redo stack to current state. */
    public void redo() {
        if(redo.empty()) return;
        undo.push(current);
        current = redo.pop();
    }
    
    /** Returns the image associated with the current state. */
    public BufferedImage getImage() {return current.cache();}
}