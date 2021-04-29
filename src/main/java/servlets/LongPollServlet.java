package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import game.Player;
import sessionAttributes.SessionAttributeKeys;

@WebServlet("/LongPollServlet")
public class LongPollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
                         throws ServletException,IOException {
		try {
            ((Player)request.getSession().getAttribute(SessionAttributeKeys.Player.toString()))
            .eventQ.take().toJson(request,response);
        } catch(final Exception e) {e.printStackTrace();}
	}
}