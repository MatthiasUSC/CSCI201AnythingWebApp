package resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public final class Registry {
    
    public static final Path getResource(final String path) {
        try {return Paths.get(Registry.class.getResource(path).toURI());}
        catch(final URISyntaxException e) {e.printStackTrace(); System.exit(1); return null;}
    }
    
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
    
    public static final MongoClient MONGO;
    
    static {
        String s = "";
        try(final BufferedReader i = new BufferedReader(new FileReader(getResource("mongo.txt").toFile()))) {s = i.readLine();}
        catch(final IOException e) {e.printStackTrace(); System.out.println("Could not get the MongoDB info."); System.exit(1);}
        MONGO  = MongoClients.create(s);
    }
}