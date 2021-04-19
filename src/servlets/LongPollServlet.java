package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LongPollServlet
 */
@WebServlet("/LongPollServlet")
public class LongPollServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// wait here until Game instance updates
		// have sessionAttribute boolean or something  (canUpdate)
		boolean canUpdate = true;
		while(!canUpdate) {
			//either thread sleep here, or thread yield, sleep might be more efficient performance wise
		}
		canUpdate = false;
		
		// or just use a lock and wait() and notifyAll()
		
		// or just have longpoll get game state every half second or so
		
		
		// get game state down here
		String gameDataJson = "";
		response.getWriter().append(gameDataJson);
	}

}
