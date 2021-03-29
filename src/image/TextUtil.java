package image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public final class TextUtil {
    
    public static enum AlignmentX {LEFT,CENTER,RIGHT}
    public static enum AlignmentY {HEAD,TOP,MID,LOW,FOOT}
    
    public static final BufferedImage align(final BufferedImage img,final int w,final int h,
                                            final AlignmentX alignX,final AlignmentY alignY,
                                            final Font f,final Color fill,final Color outline,
                                            final Color bg,final double outlineSize,
                                            final char...text) {
        final double bx,by;
        final BufferedImage txt;
        {
            txt = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g = txt.createGraphics();
            final Shape s;
            {
                TextLayout tl = null;
                float min = 1,max = 1000,scaled = f.getSize2D();
                final String str = new String(text);
                final FontRenderContext frc = g.getFontRenderContext();
                double nbx = 0,nby = 0;
                while(max - min > 1.f) {
                    final Rectangle2D r = (tl = new TextLayout(str,f.deriveFont(scaled),frc)).getBounds();
                    nbx = r.getWidth()  - outlineSize;
                    nby = r.getHeight() - outlineSize;
                    if(nbx > w || nby > h) max = scaled;
                    else min = scaled;
                    scaled = (max + min) / 2.f;
                }
                bx = nbx; by = nby;
                s = tl.getOutline(null);
                final Rectangle2D bb = s.getBounds2D();
                System.out.println(bb);
                g.translate(0,1.-bb.getMinY());
            }
            g.setColor(fill);
            g.fill(s);
            g.setColor(outline);
            g.setStroke(new BasicStroke((float)outlineSize));
            g.draw(s);
            g.dispose();
        }
        
        final boolean extended = alignY == AlignmentY.HEAD || alignY == AlignmentY.FOOT;
        final BufferedImage nimg = new BufferedImage(
            img.getWidth(),
            extended? (int)(img.getHeight()+by+.5) : img.getHeight(),
            img.getType()
        );
        final Graphics2D g = nimg.createGraphics();
        g.drawImage(img,0,extended? (int)(by+.5) : 0,null);
        g.drawImage(
            txt,
            switch(alignX) {
                case LEFT -> 0;
                case RIGHT -> (int)((double)img.getWidth() - bx + .5);
                case CENTER -> (int)(((double)img.getWidth() - bx) / 2.);
            },
            switch(alignY) {
                case HEAD,TOP -> 0;
                case MID -> (int)(((double)img.getHeight() - by + 1.) / 2.);
                case LOW -> (int)((double)img.getHeight() - by + .5);
                case FOOT -> (int)((double)img.getHeight() + by + .5);
            },
            null
        );
        g.setColor(new Color(Color.MAGENTA.getRed(),Color.MAGENTA.getGreen(),Color.MAGENTA.getBlue(),64));
        g.translate(
            switch(alignX) {
                case LEFT -> 0;
                case RIGHT -> (int)((double)img.getWidth() - bx + .5);
                case CENTER -> (int)(((double)img.getWidth() - bx) / 2.);
            },
            switch(alignY) {
                case HEAD,TOP -> 0;
                case MID -> (int)(((double)img.getHeight() - by + 1.) / 2.);
                case LOW -> (int)((double)img.getHeight() - by + .5);
                case FOOT -> (int)((double)img.getHeight() + by + .5);
            }
        );
        g.drawRect(txt.getMinX(),txt.getMinY(),txt.getWidth()-1,txt.getHeight()-1);
        g.dispose();
        return nimg;
    }
    
    private static final float INIT_FONT_SIZE = 1000f;
    public static final BufferedImage align2(final BufferedImage img,final int w,final int h,
                                             final AlignmentX alignX,final AlignmentY alignY,
                                             final Font f,final Color fill,final Color outline,
                                             final Color bg,final double outlineSize,
                                             final char...text) {
        final BufferedImage txt;
        final int x,y;
        final double w0,h0;
        {
            final double scalar;
            {
                final String str = new String(text);
                final Font derived = f.deriveFont(INIT_FONT_SIZE);
                final double nos;
                {
                    final double pa,pb;
                    {
                        final Rectangle2D r;
                        {
                            final Graphics2D g = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).createGraphics();
                            r = new TextLayout(str,derived,g.getFontRenderContext()).getBounds();
                            g.dispose();
                        }
                        /*\
                         * Calculating the scalar:
                         * 
                         * a: bounding size
                         * b: unscaled size
                         * s: scalar
                         * o: unscaled outline
                         * s = a / (b + 2 * o / s) => s = (a - 2 * o) / b
                         * 
                         * o': scaled outline
                         * o' = o / s
                        \*/
                        {
                            final double sx = (double)w / (pa = r.getWidth());
                            final double sy = (double)h / (pb = r.getHeight());
                            final boolean which = sx > sy;
                            final double a = (double)(which? h : w) - 2. * outlineSize;
                            scalar = (a > 0? a : 1.) / (which? pb : pa);
                        }
                        nos = outlineSize / scalar;
                    }
                    {
                        final int nw,nh;
                        txt = new BufferedImage(nw = (int)(pa+nos+.5),nh = (int)(pb+nos+.5),BufferedImage.TYPE_INT_ARGB);
                        
                        w0 = nw * scalar;
                        h0 = nh * scalar;
                    }
                    x = switch(alignX) {
                        case LEFT -> 0;
                        case RIGHT -> (int)((double)img.getWidth() - w0 - .5);
                        case CENTER -> (int)(((double)img.getWidth() - w0) / 2. + .5);
                    };
                    y = switch(alignY) {
                        case HEAD,TOP -> 0;
                        case MID -> (int)(((double)img.getHeight() - h0) / 2. + .5);
                        case LOW -> (int)((double)img.getHeight() - h0 - .5);
                        case FOOT -> (int)((double)img.getHeight() + (h0 % 1) + .5);
                    };
                }
                {
                    final Graphics2D g = txt.createGraphics();
                    {
                        final Shape s = new TextLayout(str,derived,g.getFontRenderContext()).getOutline(null);
                        {
                            final Rectangle2D bb = s.getBounds2D();
                            g.translate(nos / 2. - bb.getMinX(),nos / 2. - bb.getMinY());
                        }
                        g.setColor(outline);
                        {
                            final Stroke stk = g.getStroke();
                            g.setStroke(new BasicStroke((float)nos));
                            g.draw(s);
                            g.setStroke(stk);
                        }
                        g.setColor(fill);
                        g.fill(s);
                    }
                    g.dispose();
                }
            }
            // DEBUG
            //try {ImageIO.write(txt,"png",new File("text.png"));}
            //catch(final IOException e) {e.printStackTrace();}
        }
        if(alignY == AlignmentY.HEAD || alignY == AlignmentY.FOOT) {
            final BufferedImage nimg = new BufferedImage(img.getWidth(),img.getHeight()+h,img.getType());
            final Graphics2D g = nimg.createGraphics();
            g.setColor(bg);
            g.fillRect(0,0,nimg.getWidth(),nimg.getHeight());
            g.drawImage(txt,x,y,(int)(w0+.5),(int)(h0+.5),null);
            g.drawImage(img,0,alignY == AlignmentY.HEAD? h : 0,null);
            g.dispose();
            return nimg;
        }
        
        final Graphics2D g = img.createGraphics();
        g.drawImage(txt,x,y,(int)(w0+.5),(int)(h0+.5),null);
        g.dispose();
        
        return img;
    }
    
    /**
     * @param w           Bounding width
     * @param h           Bounding height
     * @param outlineSize Unscaled outline size.
     * @param fill        Fill color.
     * @param outline     Outline color
     * @param f           Font.
     * @param text        Text.
     * 
     * @return An image representation of the text, with an outline scaled to the
     *         bounding size.
     */
    public static final BufferedImage charsToImage(final int w,final int h,final double outlineSize,
                                                   final Color fill,final Color outline,
                                                   final Font f,final int style,final char...text) {
        final BufferedImage txt;
        final double scalar;
        final String str = new String(text);
        final Font derived = f.deriveFont(style,INIT_FONT_SIZE);
        {
            final double pa,pb;
            {
                final boolean which;
                {
                    final Rectangle2D r;
                    {
                        final Graphics2D g = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).createGraphics();
                        r = new TextLayout(str,derived,g.getFontRenderContext()).getBounds();
                        g.dispose();
                    }
                    which = ((double)w / (pa = r.getWidth())) > ((double)h / (pb = r.getHeight()));
                }
                /*\
                 * Calculating the scalar:
                 * 
                 * a: bounding size
                 * b: unscaled size
                 * s: scalar
                 * o: unscaled outline
                 * s = a / (b + 2 * o / s) => s = (a - 2 * o) / b
                 * 
                 * o': scaled outline
                 * o' = o / s
                \*/
                final double a = (double)(which? h : w) - 2. * outlineSize;
                scalar = (which? pb : pa) * outlineSize / (a > 0? a : 1.);
            }
            txt = new BufferedImage((int)(pa+scalar+.5),(int)(pb+scalar+.5),BufferedImage.TYPE_INT_ARGB);
        }
        {
            final Graphics2D g = txt.createGraphics();
            {
                final Shape s = new TextLayout(str,derived,g.getFontRenderContext()).getOutline(null);
                {
                    final Rectangle2D bb = s.getBounds2D();
                    g.translate(scalar / 2. - bb.getMinX(),scalar / 2. - bb.getMinY());
                }
                g.setColor(outline);
                {
                    final Stroke stk = g.getStroke();
                    g.setStroke(new BasicStroke((float)scalar));
                    g.draw(s);
                    g.setStroke(stk);
                }
                g.setColor(fill);
                g.fill(s);
            }
            g.dispose();
        }
        return txt;
    }
    
}




























