package servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import contextAttributes.ContextAttributeKeys;
import contextAttributes.GameManager;
import game.Room;
import sessionAttributes.SessionAttributeKeys;

/*class CreateRoomResponse {
	int status;
	long gameCode;
	public CreateRoomResponse(int status, long gameCode) {
		this.status = status;
		this.gameCode = gameCode;
	}
}*/

/**
 * Servlet implementation class CreateRoomServlet
 * 
 * Status codes:
 * 0 is success and gamecode is okay to read
 * 1 is unknown exception
 * 2 is interrupted exception
 * 
 * TODO ought to make the response system better
 */
@WebServlet("/CreateRoomServlet")
@MultipartConfig
public class CreateRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String FILES_PARAMETER = "file";
	private static final String MAX_PLAYERS_PARAMETER = "max_players"; // subject to change
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			// recieve room params from client
			//List<BufferedImage> images = getImages(request);
			
			HttpSession session = request.getSession();
			ServletContext context = request.getServletContext();
			GameManager gameManager = (GameManager)context.getAttribute(ContextAttributeKeys.GameManager.toString());
			
			// TODO check if already in a room? or playing a game? how to handle, just leave other game, or throw error?
			
			// create room and join
			//BufferedImage[] bufferedImages = (BufferedImage[])images.toArray();
			Room room = Room.createRoom((byte)10, 300 * 1000, (byte)10,getImages(request).toArray(BufferedImage[]::new));
			//Player player = room.addPlayer();
			
			// add to game manager for other players to access
			long gameCode = gameManager.addGame(room);
			
			// link room and player to session
			session.setAttribute(SessionAttributeKeys.Room.toString(), room);
			//session.setAttribute(SessionAttributeKeys.PLAYER, player);
			session.setAttribute(SessionAttributeKeys.Player.toString(),room.addPlayer());
			
			setResponse(response, 0, gameCode);
		} catch(InterruptedException e) {
			e.printStackTrace();
			setResponse(response, 2);
		} catch(Exception e) { //TODO is this necessary?
			e.printStackTrace();
			setResponse(response, 1);
		}
	}
	
	private static void setResponse(HttpServletResponse response, int status) throws IOException {
	    //TODO do we need this extra function call?
		setResponse(response, status, 0);
	}
	
	private static void setResponse(HttpServletResponse response, int status, long gameCode) throws IOException{
		/*CreateRoomResponse r = new CreateRoomResponse(status, gameCode);
		Gson gson = new Gson();
		String json = gson.toJson(r);*/
		
		response.resetBuffer();
		//response.getWriter().append(json);
		//This is significantly faster than GSON
		response.getWriter().append("{\"status\":")
		                    .append(Integer.toString(status))
		                    .append(",\"gameCode\":")
		                    .append(Long.toString(gameCode))
		                    .append('}');
	}
	
	private static List<BufferedImage> getImages(final HttpServletRequest request)
	                                             throws ServletException,IOException {
		//ArrayList<BufferedImage> out = new ArrayList<BufferedImage>();
		//List<Part> fileParts = request.getParts().stream().filter(part -> FILES_PARAMETER.equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList()); 
		
	    /*for (Part filePart : fileParts) {
	        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix. TODO needed?
	        InputStream fileContent = filePart.getInputStream();
	        
	     	// converting to buffered image
	        BufferedImage newBi = ImageIO.read(fileContent);
	        out.add(newBi);
	    }*/
	    
	    final ArrayList<BufferedImage> out = new ArrayList<>();
	    for(final Part filePart : request.getParts()
	                                     .stream()
	                                     .filter(part -> FILES_PARAMETER.contentEquals(part.getName()))
	                                     .collect(Collectors.toList())) {
	        out.add(ImageIO.read(filePart.getInputStream()));
	    }
	    
	    return out;
	}
}
