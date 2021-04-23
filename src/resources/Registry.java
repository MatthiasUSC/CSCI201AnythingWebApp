package resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public final class Registry {
    
    public static final Path getResource(final String...path)
    {return Paths.get(Path.of(System.getProperty("user.dir"),"src","resources").toString(),path);}
    
    public static final Font IMPACT_FONT;
    
    static {
        Font f = null;
        try {f = Font.createFont(Font.TRUETYPE_FONT,getResource("impact.ttf").toFile());}
        catch(final FontFormatException | IOException e) {
            e.printStackTrace();
            System.err.println("Could not import the meme font :(");
            System.exit(1);
        }
        IMPACT_FONT = f;
    }
    
    public static final Map<String,Font> FONTS;
    
    static {
        FONTS = new TreeMap<>();
        for(final String fam : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
            FONTS.put(fam,new Font(fam,Font.PLAIN,1));
    }
    
    public static final float DEFAULT_OUTLINE_SIZE = 3.5f;
    
    public static final Map<String,Integer> STYLES;
    static {
        STYLES = new TreeMap<>();
        STYLES.put("bold",Font.BOLD);
        STYLES.put("italic",Font.ITALIC);
        STYLES.put("plain",Font.PLAIN);
    }
}





















































