package servlet.game;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/GameServlet")
public class GameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static enum Operations {
        submit_image(SubmitImageServlet.class),
        judge_image(JudgeImageServlet.class),
        preview_meme(PreviewMemeServlet.class),
        leave_room(LeaveRoomServlet.class);
        
        private String dispatcher;
        private Operations(final Class<?> dispatcher) {this.dispatcher = dispatcher.getSimpleName();}
        private void forward(final HttpServletRequest request,
                             final HttpServletResponse response)
                             throws ServletException,IOException {
            request.getRequestDispatcher(dispatcher).forward(request,response);
        }
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// TODO check if in room
		// return error if not in room and have player
		
		/*String operation request.getParameter("op");
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
		}*/
		try {Operations.valueOf(request.getParameter("op")).forward(request,response);}
		catch(IllegalArgumentException|NullPointerException e) {
		    //TODO catch if "op" param is not in the enum
		} catch(Exception e) {
		    //TODO catch other stuff ig
		}
	}

}
