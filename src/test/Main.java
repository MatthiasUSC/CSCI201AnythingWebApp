package test;

import static resources.Registry.IMPACT_FONT;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import image.ImageHistory;
import image.TextUtil;
import image.TextUtil.AlignmentX;
import image.TextUtil.AlignmentY;
import resources.Registry;

public class Main {
    private static void drawString(final double x,final double y,
                                   final String str,
                                   final Color fill,
                                   final Color outline,
                                   final BufferedImage img,
                                   final double outlineSize,
                                   final Font font) {
        final Graphics2D g = img.createGraphics();
        final TextLayout tl = new TextLayout(str,font,g.getFontRenderContext());
        
        {
            final Rectangle2D r = tl.getBounds();
            final int n = (int)(x+r.getMinX()-outlineSize+.5),
                      w = (int)(r.getWidth()+outlineSize+.5),
                      s = (int)(y+r.getMinY()-outlineSize+.5),
                      h = (int)(r.getHeight()+outlineSize+.5);
            g.translate(x-r.getMinX()+outlineSize-.5,y-r.getMinY()+outlineSize-.5);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1));
            g.drawRect(n,s,w,h);
        }
        
        g.setColor(fill);
        final Shape ol = tl.getOutline(null);
        g.fill(ol);
        g.setStroke(new BasicStroke((float)outlineSize));
        g.setColor(outline);
        g.draw(ol);
    }
    
    private static void draw1() {
        final BufferedImage img = new BufferedImage(2510,1000,BufferedImage.TYPE_INT_ARGB);
        
        drawString(0,0,"ree",Color.RED,Color.BLUE,img,3.5f,IMPACT_FONT.deriveFont(Font.BOLD,80));
        
        try {
            ImageIO.write(img,"png",new File("ree.png"));
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void draw2() {
        //final BufferedImage img = new BufferedImage(2400,1000,BufferedImage.TYPE_INT_ARGB);
        try {
            final BufferedImage img = ImageIO.read(Registry.getResource("IMG-0111.JPG").toFile());
            
            final BufferedImage nimg = TextUtil.align2(img,img.getWidth(),100,AlignmentX.CENTER,AlignmentY.MID,IMPACT_FONT,
                                                            Color.WHITE,Color.BLACK,Color.DARK_GRAY,3.5,"abcdefghijklmnopqrstuvwxyzGGE".toCharArray());
            ImageIO.write(nimg,"png",new File("nree.png"));
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }
    private static void draw3() {
        //final BufferedImage img = new BufferedImage(2400,1000,BufferedImage.TYPE_INT_ARGB);
        try {
            final BufferedImage img = ImageIO.read(Registry.getResource("IMG-0111.JPG").toFile());
            
            BufferedImage nimg = TextUtil.align2(img,img.getWidth(),100,AlignmentX.CENTER,AlignmentY.TOP,IMPACT_FONT,
                                                      Color.WHITE,Color.BLACK,Color.DARK_GRAY,3.5,"TFW".toCharArray());
            nimg = TextUtil.align2(nimg,nimg.getWidth(),100,AlignmentX.CENTER,AlignmentY.LOW,IMPACT_FONT,
                                        Color.WHITE,Color.BLACK,Color.DARK_GRAY,3.5,"Code Works".toCharArray());
            
            nimg = TextUtil.align2(nimg,nimg.getWidth(),20,AlignmentX.LEFT,AlignmentY.HEAD,IMPACT_FONT,
                                        Color.WHITE,Color.BLACK,Color.DARK_GRAY,3.5,"Header".toCharArray());
            
            nimg = TextUtil.align2(nimg,nimg.getWidth(),15,AlignmentX.RIGHT,AlignmentY.FOOT,IMPACT_FONT,
                                        Color.WHITE,Color.BLACK,Color.DARK_GRAY,3.5,"Footer".toCharArray());
            
            ImageIO.write(nimg,"png",new File("super_happy.png"));
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void draw4() {
        try {
            BufferedImage img = ImageIO.read(Registry.getResource("IMG-0111.JPG").toFile());
            final Color[][] c = new Color[][]{{Color.WHITE,Color.BLACK,Color.RED,Color.BLUE,Color.GREEN},
                                              {Color.BLACK,Color.WHITE,Color.YELLOW,Color.MAGENTA,Color.ORANGE},
                                              {Color.DARK_GRAY,Color.LIGHT_GRAY}};
            final AlignmentY[] Y = new AlignmentY[] {AlignmentY.TOP,AlignmentY.MID,AlignmentY.LOW};
            final long t0 = System.currentTimeMillis();
            char n = 0;
            for(final AlignmentY y : Y)
                for(final AlignmentX x : AlignmentX.values())
                    img = TextUtil.align2(img,img.getWidth(),50,x,y,IMPACT_FONT,c[0][n%5],c[1][n%5],c[2][n%2],++n,(char)('0'+n));
            n = (char)-1;
            img = TextUtil.align2(img,img.getWidth(),30,AlignmentX.LEFT,  AlignmentY.HEAD,IMPACT_FONT,c[0][n%5],c[1][n%5],c[2][n%2],3.5,(char)('A'+(++n)));
            img = TextUtil.align2(img,img.getWidth(),30,AlignmentX.CENTER,AlignmentY.TOP, IMPACT_FONT,c[0][n%5],c[1][n%5],c[2][n%2],3.5,(char)('A'+(++n)));
            img = TextUtil.align2(img,img.getWidth(),30,AlignmentX.RIGHT, AlignmentY.TOP, IMPACT_FONT,c[0][n%5],c[1][n%5],c[2][n%2],3.5,(char)('A'+(++n)));
            img = TextUtil.align2(img,img.getWidth(),30,AlignmentX.LEFT,  AlignmentY.FOOT,IMPACT_FONT,c[0][n%5],c[1][n%5],c[2][n%2],3.5,(char)('A'+(++n)));
            img = TextUtil.align2(img,img.getWidth(),30,AlignmentX.CENTER,AlignmentY.LOW, IMPACT_FONT,c[0][n%5],c[1][n%5],c[2][n%2],3.5,(char)('A'+(++n)));
            img = TextUtil.align2(img,img.getWidth(),30,AlignmentX.RIGHT, AlignmentY.LOW, IMPACT_FONT,c[0][n%5],c[1][n%5],c[2][n%2],3.5,(char)('A'+(++n)));
            final long tm = System.currentTimeMillis();
            ImageIO.write(img,"png",new File("super_happy.png"));
            final long t1 = System.currentTimeMillis();
            System.out.println("Image processing: "+(tm-t0));
            System.out.println("   Serialization: "+(t1-tm));
            System.out.println("           Total: "+(t1-t0));
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void draw5() {
        try {
            BufferedImage img = TextUtil.charsToImage(new int[] {100,100},3.5,Color.WHITE,Color.BLACK,IMPACT_FONT,Font.PLAIN,"ree".toCharArray());
            ImageIO.write(img,"png",new File("text2.png"));
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void draw6() {
        try {
            final ImageHistory ih = new ImageHistory(ImageIO.read(Registry.getResource("IMG-0111.JPG").toFile()));
            ImageIO.write(ih.getImage(),"png",new File("test/debug.png"));
            final Color f = Color.WHITE,o = Color.BLACK;
            ih.pushState(AlignmentY.TOP,AlignmentX.LEFT,50,3.5,f,o,null,null,IMPACT_FONT,Font.PLAIN,"test a1".toCharArray());
            ih.pushState(AlignmentY.MID,AlignmentX.CENTER,50,3.5,f,o,null,null,IMPACT_FONT,Font.PLAIN,"test a2".toCharArray());
            ih.pushState(AlignmentY.LOW,AlignmentX.RIGHT,50,3.5,f,o,null,null,IMPACT_FONT,Font.PLAIN,"test a3".toCharArray());
            ImageIO.write(ih.getImage(),"png",new File("test/test_a.png"));
            ih.pushState(AlignmentY.TOP,AlignmentX.RIGHT,40,3.5,f,o,null,null,IMPACT_FONT,Font.PLAIN,"test b1".toCharArray());
            ih.pushState(AlignmentY.MID,AlignmentX.CENTER,40,3.5,f,o,null,null,IMPACT_FONT,Font.PLAIN,"test b2".toCharArray());
            ImageIO.write(ih.getImage(),"png",new File("test/test_b.png"));
            ih.undo();
            ImageIO.write(ih.getImage(),"png",new File("test/test_c.png"));
            ih.redo();
            ImageIO.write(ih.getImage(),"png",new File("test/test_d.png"));
            ih.pushState(AlignmentY.HEAD,AlignmentX.CENTER,30,3.5,f,o,null,null,IMPACT_FONT,Font.PLAIN,"test e1".toCharArray());
            ImageIO.write(ih.getImage(),"png",new File("test/test_e.png"));
            ih.undo();
            ih.pushState(AlignmentY.FOOT,AlignmentX.CENTER,30,3.5,f,o,Color.CYAN,Color.YELLOW,IMPACT_FONT,Font.PLAIN,"test f1".toCharArray());
            ImageIO.write(ih.getImage(),"png",new File("test/test_f.png"));
            ih.pushState(AlignmentY.HEAD,AlignmentX.CENTER,30,3.5,f,o,Color.CYAN,Color.YELLOW,IMPACT_FONT,Font.PLAIN,"test g1".toCharArray());
            ImageIO.write(ih.getImage(),"png",new File("test/test_g.png"));
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(final String[] args) {
        //draw1();
        //draw2();
        //draw3();
        //draw4();
        //draw5();
        draw6();
    }
}



































