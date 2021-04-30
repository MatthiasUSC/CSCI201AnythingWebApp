package servlet.game;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import game.Room;
import sessionAttributes.SessionAttributeKeys;

@WebServlet("/JudgeImageServlet")
public class JudgeImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String WINNER_PARAM = "winner";
    
	@Override
	protected void service(final HttpServletRequest request,
	                       final HttpServletResponse response)
	                       throws ServletException,IOException {
	    try {
	        ((Room)request.getSession().getAttribute(SessionAttributeKeys.Room.toString()))
	        .setWinner(Byte.parseByte(request.getParameter(WINNER_PARAM)));
	    } catch(final Exception e) {e.printStackTrace();}
	}
}