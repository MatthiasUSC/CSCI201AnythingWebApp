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
import javax.servlet.http.HttpSession;

import sessionAttributes.SessionAttributeKeys;

interface Action {CharSequence action(final Room r,final Player p) throws IOException;}
public enum GameState {
    JOIN((r,p) -> new StringBuilder(",\"code\":").append(r.code).append(',').append(r.playerJSON())),
    START((r,p) -> new StringBuilder(",\"judge\":").append(p.id == r.getJudge()).append(",\"image\":\"").append(encode(r.getRoundImage())).append('"')),
    TIMEOUT((r,p) -> {
        final StringJoiner join = new StringJoiner("\",\"","\"","\"");
        for(final byte i : r.scramble) join.add(encode(r.finished[i]));
        return new StringBuilder(",\"images\":[").append(join.toString()).append(']').append(",\"judge\":").append(p.id == r.getJudge());
    }),
    JUDGE((r,p) -> new StringBuilder(",\"winner\":\"").append(encode(r.winner)).append('"')),
    END((r,p) -> "");
    
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
                     .append(action.action(
                         (Room)i.getSession().getAttribute(SessionAttributeKeys.Room.toString()),
                         (Player)i.getSession().getAttribute(SessionAttributeKeys.Player.toString())
                     ))
                     .append('}');
        if(this == END) {
            final HttpSession s = i.getSession();
            s.removeAttribute(SessionAttributeKeys.Room.toString());
            s.removeAttribute(SessionAttributeKeys.Player.toString());
        }
    }
}