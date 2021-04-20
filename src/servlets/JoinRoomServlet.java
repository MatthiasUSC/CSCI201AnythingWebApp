package servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import contextAttributes.ContextAttributeKeys;
import contextAttributes.GameManager;
import game.Player;
import game.Room;
import sessionAttributes.SessionAttributeKeys;

/**
 * Servlet implementation class JoinRoomServlet
 */
@WebServlet("/JoinRoomServlet")
public class JoinRoomServlet extends HttpServlet {
	
	private static final String ROOM_CODE_PARAMETER = "room_code"; // subject to change
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// recieve room params from client
			Integer roomCode = Integer.parseInt(request.getParameter(ROOM_CODE_PARAMETER));
			
			HttpSession session = request.getSession();
			ServletContext context = request.getServletContext();
			GameManager gameManager = (GameManager)context.getAttribute(ContextAttributeKeys.GAME_MANAGER);
			
			// TODO check if already in a room? or playing a game? how to handle, just leave other game, or throw error?
			
			// try find room and join
			Room room = gameManager.getGame(roomCode);
			Player player = room.addPlayer();
			
			// link room and player to session
			session.setAttribute(SessionAttributeKeys.ROOM, room);
			session.setAttribute(SessionAttributeKeys.PLAYER, player);
			
			response.getWriter().append("success");
		} catch(InterruptedException e) {
			e.printStackTrace();
			response.getWriter().append(e.getMessage());
		} catch(NumberFormatException e) {
			e.printStackTrace();
			response.getWriter().append(e.getMessage());
		} catch(GameManager.GameNotFoundException e) {
			e.printStackTrace();
			response.getWriter().append(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			response.getWriter().append(e.getMessage());
		}
	}

}
