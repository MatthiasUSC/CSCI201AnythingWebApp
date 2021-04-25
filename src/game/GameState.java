package game;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.StringJoiner;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sessionAttributes.SessionAttributeKeys;

interface Action {CharSequence action(final Room i) throws IOException;}
public enum GameState {
    JOIN(r -> ',' + r.playerJSON()),
    START(r -> new StringBuilder(",\"image\":\"").append(encode(r.getRoundImage())).append('"')),
    TIMEOUT(r -> {
        final StringJoiner join = new StringJoiner("\",\"","\"","\"");
        for(final byte i : r.scramble) join.add(encode(r.finished[i]));
        return new StringBuilder(",\"images\":[").append(join.toString()).append(']');
    }),
    JUDGE(r -> "\"winner\":" + Integer.toString(r.getWinner())),
    END(r -> "");
    
    private static final Encoder B64 = Base64.getEncoder();
    private static String encode(final BufferedImage i) throws IOException {
        final ByteArrayOutputStream o = new ByteArrayOutputStream();
        ImageIO.write(i,"PNG",o);
        return B64.encodeToString(o.toByteArray());
    }
    private final Action action;
    private GameState(final Action action) {this.action = action;}
    public void toJson(final HttpServletRequest i,
                       final HttpServletResponse o)
                       throws ServletException,IOException {
        o.getWriter().append("{\"event\":\"")
                     .append(toString())
                     .append("\"")
                     .append(action.action((Room)i.getSession().getAttribute(SessionAttributeKeys.Room.toString())))
                     .append('}');
    }
}


























