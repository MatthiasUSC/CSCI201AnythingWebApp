package servlet.game;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.StringJoiner;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import game.Room;
import sessionAttributes.SessionAttributeKeys;

@WebServlet("/GameLoopServlet")
public class GameLoopServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CONTROL_PARAM = "control";
    private static final Encoder B64 = Base64.getEncoder();
    
    private static interface control {void action(final Room r,final HttpServletResponse o) throws IOException;}
    private static enum Control {
        start((r,o) -> r.start()),
        startImage((r,o) -> {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(r.getRoundImage(),"PNG",out);
            o.getWriter().append("{\"image\":\"")
                         .append(B64.encodeToString(out.toByteArray()))
                         .append("\"}");
        }),
        endImages((r,o) -> {
            final StringJoiner join = new StringJoiner("\",\"","\"","\"");
            for(final byte i : r.scramble) {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(r.finished[i],"PNG",out);
                join.add(B64.encodeToString(out.toByteArray()));
            }
            o.getWriter().append("{\"images\":[")
                         .append(join.toString())
                         .append("]}");
        }),
        winner((r,o) -> {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(r.finished[r.getWinner()],"PNG",out);
            o.getWriter().append("{\"id\":")
                         .append(Integer.toString(r.getWinner()))
                         .append(",\"img\":\"")
                         .append(B64.encodeToString(out.toByteArray()))
                         .append("\"}");
        });
        
        private final control action;
        private Control(final control a) {action = a;}
        private void action(final Room r,final HttpServletResponse o) throws IOException {action.action(r,o);}
    }
    
    public GameLoopServlet() {super();}
    
    @Override
    protected void service(final HttpServletRequest request,
                           final HttpServletResponse response)
                           throws ServletException,IOException {
        try {
            Control.valueOf(request.getParameter(CONTROL_PARAM)).action(
                (Room)request.getSession().getAttribute(SessionAttributeKeys.Room.toString()),
                response
            );
        } catch(Exception e) {
            
        }
    }
    
}























