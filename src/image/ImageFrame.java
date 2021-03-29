package image;

import static java.lang.System.arraycopy;
import static util.ArrayUtil.fastArrayFill;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import image.TextUtil.AlignmentX;
import image.TextUtil.AlignmentY;
import resources.Registry;
import util.LazilyLoadedObject;
import util.LazilyLoadedObject.Copier;
import util.LazilyLoadedObject.Instantiator;

public class ImageFrame {
    private static enum TextColorPart {outline,fill}
    private static enum Margin {header,footer}
    
    private static final int N_VALIGN = AlignmentY.values().length;
    private static final int N_TEXT_COLOR_PARTS = TextColorPart.values().length;
    private static final int N_MARGINS = Margin.values().length;
    
    private static final int[] DEFAULT_TEXT_COLORS = new int[] {Color.BLACK.getRGB(),Color.WHITE.getRGB()};
    private static final int[] DEFAULT_BG = new int[] {Color.WHITE.getRGB(),Color.WHITE.getRGB()};
    private static final int[] DEFAULT_STYLES;
    private static final float[] DEFAULT_OUTLINE;
    private static final int[] DEFAULT_SIZES;
    private static final AlignmentX[] DEFAULT_HALIGN;
    private static final Font[] DEFAULT_FONTS;
    
    static {
        DEFAULT_HALIGN = new AlignmentX[N_VALIGN];
        fastArrayFill(DEFAULT_HALIGN,AlignmentX.CENTER);
        
        DEFAULT_FONTS = new Font[N_VALIGN];
        fastArrayFill(DEFAULT_FONTS,Registry.IMPACT_FONT);
        
        DEFAULT_STYLES = fastArrayFill(N_VALIGN,Font.PLAIN);
        
        DEFAULT_OUTLINE = fastArrayFill(N_VALIGN,Registry.DEFAULT_OUTLINE_SIZE);
        
        DEFAULT_SIZES = new int[N_VALIGN];
        for(final AlignmentY y : AlignmentY.values()) {
            DEFAULT_SIZES[y.ordinal()] = switch(y) {
                case HEAD,FOOT -> 20;
                default -> 100;
            };
        }
    }
    
    private static final class State {
        private static final Copier<AlignmentX[]> CPY_HALIGN = (s,d) -> arraycopy(s,0,d,0,N_VALIGN);
        private static final Instantiator<AlignmentX[]> INST_HALIGN = () -> {
            final AlignmentX[] v = new AlignmentX[N_VALIGN];
            arraycopy(DEFAULT_HALIGN,0,v,0,N_VALIGN);
            return v;
        };
        
        private static final Copier<BufferedImage[]> CPY_OVERLAY = (s,d) -> arraycopy(s,0,d,0,N_VALIGN);
        private static final Instantiator<BufferedImage[]> INST_OVERLAY = () -> new BufferedImage[N_VALIGN];
        
        private static final Copier<int[]> CPY_BG = (s,d) -> arraycopy(s,0,d,0,N_VALIGN);
        private static final Instantiator<int[]> INST_BG = () -> {
            final int[] v = new int[N_MARGINS];
            arraycopy(DEFAULT_BG,0,v,0,N_MARGINS);
            return v;
        };
        
        private static final Copier<Font[]> CPY_FONTS = (s,d) -> arraycopy(s,0,d,0,N_VALIGN);
        private static final Instantiator<Font[]> INST_FONTS = () -> {
            final Font[] v = new Font[N_VALIGN];
            arraycopy(DEFAULT_FONTS,0,v,0,N_VALIGN);
            return v;
        };
        
        private static final Copier<int[]> CPY_STYLES = (s,d) -> arraycopy(s,0,d,0,N_VALIGN);
        private static final Instantiator<int[]> INST_STYLES = () -> {
            final int[] v = new int[N_VALIGN];
            arraycopy(DEFAULT_STYLES,0,v,0,N_VALIGN);
            return v;
        };
        
        private static final Copier<float[]> CPY_OUTLINE = (s,d) -> arraycopy(s,0,d,0,N_VALIGN);
        private static final Instantiator<float[]> INST_OUTLINE = () -> {
            final float[] v = new float[N_VALIGN];
            arraycopy(DEFAULT_OUTLINE,0,v,0,N_VALIGN);
            return v;
        };
        
        private static final Copier<int[]> CPY_SIZES = (s,d) -> arraycopy(s,0,d,0,N_VALIGN);
        private static final Instantiator<int[]> INST_SIZES = () -> {
            final int[] v = new int[N_VALIGN];
            arraycopy(DEFAULT_SIZES,0,v,0,N_VALIGN);
            return v;
        };
        
        private static final Copier<char[]> CPY_TEXT = (s,d) -> arraycopy(s,0,d,0,s.length);
        private static final Instantiator<char[]> INST_TEXT = () -> null;
        
        private static final Copier<int[]> CPY_COLORS = (s,d) -> arraycopy(s,0,d,0,N_TEXT_COLOR_PARTS);
        private static final Instantiator<int[]> INST_COLORS = () -> {
            final int[] v = new int[N_MARGINS];
            arraycopy(DEFAULT_TEXT_COLORS,0,v,0,N_TEXT_COLOR_PARTS);
            return v;
        };
        
        private State n;
        private long changes = 0;
        
        private LazilyLoadedObject<AlignmentX[]> halign;
        private LazilyLoadedObject<BufferedImage[]> overlay;
        private LazilyLoadedObject<int[]> bg;
        private LazilyLoadedObject<Font[]> fonts;
        private LazilyLoadedObject<int[]> styles;
        private LazilyLoadedObject<float[]> outlines;
        private LazilyLoadedObject<int[]> sizes;
        private LazilyLoadedObject<char[]>[] text;
        private LazilyLoadedObject<int[]>[] colors;
        
        @SuppressWarnings("unchecked")
        private State(final State n) {
            this.n = n;
            halign   = new LazilyLoadedObject<>(n.halign,  CPY_HALIGN, INST_HALIGN);
            overlay  = new LazilyLoadedObject<>(n.overlay, CPY_OVERLAY,INST_OVERLAY);
            bg       = new LazilyLoadedObject<>(n.bg,      CPY_BG,     INST_BG);
            fonts    = new LazilyLoadedObject<>(n.fonts,   CPY_FONTS,  INST_FONTS);
            styles   = new LazilyLoadedObject<>(n.styles,  CPY_STYLES, INST_STYLES);
            outlines = new LazilyLoadedObject<>(n.outlines,CPY_OUTLINE,INST_OUTLINE);
            sizes    = new LazilyLoadedObject<>(n.sizes,   CPY_SIZES,  INST_SIZES);
            text     = new LazilyLoadedObject[N_VALIGN];
            colors   = new LazilyLoadedObject[N_VALIGN];
            for(int i = 0;i < N_VALIGN;++i) {
                text  [i] = new LazilyLoadedObject<char[]>(n.text  [i],CPY_TEXT,  INST_TEXT);
                colors[i] = new LazilyLoadedObject<int []>(n.colors[i],CPY_COLORS,INST_COLORS);
            }
        }
    }
    
    private final BufferedImage base;
    private BufferedImage img;
    private State current;
    
    public ImageFrame(final BufferedImage image) {
        img = new BufferedImage((base = image).getColorModel(),
                                base.copyData(null),
                                base.getColorModel().isAlphaPremultiplied(),
                                null);
        current = new State(null);
    }
    
    private State mutate(final AlignmentY y) {
        current.changes |= 1L << (long)y.ordinal();
        return current;
    }
    private State mutate(final Margin m) {
        return mutate(switch(m) {
            case header -> AlignmentY.HEAD;
            case footer -> AlignmentY.FOOT;
        });
    }
    
    public void undo() {
        final State n = current.n;
        if(n != null) {
            current.n = null;
            current = n;
        }
    }
    
    public void setHAlign (final AlignmentX x,     final AlignmentY y) {mutate(y).halign  .write(v -> v[y.ordinal()] = x);}
    public void setOverlay(final BufferedImage txt,final AlignmentY y) {current  .overlay .write(v -> v[y.ordinal()] = txt);}
    public void setBG     (final int bg,           final Margin     m) {mutate(m).bg      .write(v -> v[m.ordinal()] = bg);}
    public void setFont   (final Font font,        final AlignmentY y) {mutate(y).fonts   .write(v -> v[y.ordinal()] = font);}
    public void setStyle  (final int style,        final AlignmentY y) {mutate(y).styles  .write(v -> v[y.ordinal()] = style);}
    public void setOutline(final float outline,    final AlignmentY y) {mutate(y).outlines.write(v -> v[y.ordinal()] = outline);}
    public void setSize   (final int size,         final AlignmentY y) {mutate(y).sizes   .write(v -> v[y.ordinal()] = size);}
    public void setText   (final char[] txt,       final AlignmentY y) {mutate(y).text  [y.ordinal()].set(txt);}
    public void setColors (final int[] colors,     final AlignmentY y) {mutate(y).colors[y.ordinal()].set(colors);}
    
    public AlignmentX    getHAlign (final AlignmentY y) {return current.halign  .read()[y.ordinal()];}
    public BufferedImage getOverlay(final AlignmentY y) {return current.overlay .read()[y.ordinal()];}
    public int           getBG     (final Margin     m) {return current.bg      .read()[m.ordinal()];}
    public Font          getFont   (final AlignmentY y) {return current.fonts   .read()[y.ordinal()];}
    public int           getStyle  (final AlignmentY y) {return current.styles  .read()[y.ordinal()];}
    public float         getOutline(final AlignmentY y) {return current.outlines.read()[y.ordinal()];}
    public int           getSize   (final AlignmentY y) {return current.sizes   .read()[y.ordinal()];}
    public char[]        getText   (final AlignmentY y) {return current.text  [y.ordinal()].read();}
    public int[]         getColors (final AlignmentY y) {return current.colors[y.ordinal()].read();}
    
    public BufferedImage getImage() {
        if(current.changes != 0) {
            final int w = base.getWidth();
            {
                final AlignmentY[] alignments = AlignmentY.values();
                int i = 0;
                final Font[] fonts = current.fonts.read();
                final int[] styles = current.styles.read();
                final float[] outlines = current.outlines.read();
                final int[] sizes = current.sizes.read();
                
                for(long changes = current.changes;changes != 0;changes >>>= 1L,++i) {
                    if((changes & 1L) != 0L) {
                        final int[] colors;
                        final char[] text;
                        final int y;
                        final AlignmentY ay = alignments[i];
                        colors = getColors(ay);
                        text = getText(ay);
                        y = ay.ordinal();
                        setOverlay(
                            TextUtil.charsToImage(
                                w,
                                sizes[y],
                                outlines[y],
                                new Color(colors[1]),
                                new Color(colors[0]),
                                fonts[y],
                                styles[y],
                                text
                            ),
                            ay
                        );
                    }
                }
            }
            final BufferedImage[] overlays = current.overlay.read();
            final int head = overlays[AlignmentY.HEAD.ordinal()] == null? 0 : overlays[AlignmentY.HEAD.ordinal()].getHeight();
            final int foot = overlays[AlignmentY.FOOT.ordinal()] == null? 0 : overlays[AlignmentY.FOOT.ordinal()].getHeight();
            final int h = base.getHeight();
            
            final Graphics2D g = (img = new BufferedImage(w,h + head + foot,base.getType())).createGraphics();
            img.setAccelerationPriority(1f);
            
        }
        return img;
    }
}




















































