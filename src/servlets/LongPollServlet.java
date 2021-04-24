package servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import contextAttributes.ContextAttributeKeys;
import contextAttributes.GameManager;
import game.GameState;
import game.Player;
import sessionAttributes.SessionAttributeKeys;

@WebServlet("/LongPollServlet")
public class LongPollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	Player player = (Player)session.getAttribute(SessionAttributeKeys.Player.toString());
    	
    	try {
    		GameState gameState = player.eventQueue.take();
    		response.getWriter().append(gameState.toJson());
    	} catch(InterruptedException e) {
    		e.printStackTrace();
    	}
	}

}
