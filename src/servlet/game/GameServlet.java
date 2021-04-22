package servlet.game;

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



class Operations {
	public static final String SUBMIT_IMAGE = "submit_image";
	public static final String JUDGE_IMAGE = "judge_image";
	public static final String PREVIEW_MEME = "preview_meme";
	public static final String LEAVE_ROOM = "leave_room";
}


@WebServlet("/GameServlet")
public class GameServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		// do check if in room
		// return error if not in room and have player
		
		String operation = request.getParameter("op");
		switch(operation) {
			case Operations.SUBMIT_IMAGE:
				request.getRequestDispatcher("SubmitImageServlet").forward(request, response);
				return;
			case Operations.JUDGE_IMAGE:
				request.getRequestDispatcher("JudgeImageServlet").forward(request, response);
				return;
			case Operations.PREVIEW_MEME:
				request.getRequestDispatcher("PreviewMemeServlet").forward(request, response);
				return;
			case Operations.LEAVE_ROOM:
				request.getRequestDispatcher("LeaveRoomServlet").forward(request, response);
				return;
			default:
				//Return error, bad op
				return;
		}
	}

}
