package servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import game.Player;
import game.Room;
import sessionAttributes.SessionAttributeKeys;

@WebServlet("/JoinRoomServlet")
public class JoinRoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String OP_PARAM = "join_op";
    private static enum Operation {create,join}
    private static final String ROOM_CODE_PARAMETER = "room_code"; // subject to change
    
    private static enum Err {
        couldNotJoin,couldNotCreate;
        
        private void respond(final HttpServletResponse r) throws IOException {
            r.getWriter().append(Integer.toString(ordinal()));
        }
    }
    
    private static final Decoder B64 = Base64.getDecoder();
    
    private static interface getter<T> {T get(final String r);}
    private enum CreateRoomArg {
        maxPlayers(r -> Byte.parseByte(r)),
        timeLimit(r -> 1000L * Long.parseLong(r)),
        rounds(r -> Integer.parseInt(r)),
        images(r -> {
            final String[] b64 = r.split(",");
            final BufferedImage[] i = new BufferedImage[b64.length];
            for(int t = 0;t < b64.length;++t) {
                try {i[t] = ImageIO.read(new ByteArrayInputStream(B64.decode(b64[t])));}
                catch(final IOException e) {}
            }
            return i;
        });
        
        final getter<?> g;
        private <T> CreateRoomArg(final getter<T> g) {this.g = g;}
        @SuppressWarnings("unchecked")
        private <T> T get(final HttpServletRequest r) {return (T)g.get(r.getParameter(toString()));}
    }
    
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
                          throws ServletException,IOException {
        try {
            final Room room = switch(Operation.valueOf(request.getParameter(OP_PARAM))) {
                case create -> Room.createRoom(
                    CreateRoomArg.maxPlayers.get(request),
                    CreateRoomArg.timeLimit.get(request),
                    CreateRoomArg.rounds.get(request),
                    CreateRoomArg.images.get(request)
                );
                case join -> Room.getLobby(Byte.parseByte(request.getParameter(ROOM_CODE_PARAMETER)));
            };
            if(room == null) {Err.couldNotJoin.respond(response); return;}
            final Player player = room.addPlayer();
            // link room and player to session
            final HttpSession session = request.getSession();
            session.setAttribute(SessionAttributeKeys.Room.toString(),room);
            session.setAttribute(SessionAttributeKeys.Player.toString(),player);
            //TODO send info to clients
        } catch(InterruptedException|NumberFormatException|NullPointerException e) {
            e.printStackTrace();
            Err.couldNotCreate.respond(response);
        } catch(final Exception e) {
            e.printStackTrace();
            Err.couldNotCreate.respond(response);
        }
    }
    
}























