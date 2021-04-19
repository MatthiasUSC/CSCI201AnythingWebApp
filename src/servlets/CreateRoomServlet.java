package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;




/**
 * Servlet implementation class CreateRoomServlet
 */
@WebServlet("/CreateRoomServlet")
@MultipartConfig
public class CreateRoomServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		// TODO simplify down image communication to using BASE64 and JSON both ways (instead of multi-part form client->server and base64 json server->client)
		// Retrieves <input type="file" name="file" multiple="true">
		List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList()); 
		
	    for (Part filePart : fileParts) {
	        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	        InputStream fileContent = filePart.getInputStream();
	        
	        // reading file into byte array
	        byte[] bytes = new byte[(int)filePart.getSize()];
	        fileContent.read(bytes, 0, bytes.length);
	        fileContent.close();
	        System.out.println("here");
	        // converting to base64
	        String base64 = Base64.getEncoder().encodeToString(bytes);
	        String data = "data:image/png;base64," + base64;
	        
		    response.getWriter().append("<img src='" + data + "' width='500' height='600' />");
	    }
	}
}
