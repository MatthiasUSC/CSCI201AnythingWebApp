package servlet.game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import game.Room;
import image.TextUtil;
import sessionAttributes.SessionAttributeKeys;

import static resources.Registry.IMPACT_FONT;

@WebServlet("/PreviewMemeServlet")

class PreviewMemeResponse {
	public final int status;
	public final String base64Img;

	public PreviewMemeResponse(int status, String base64Img) {
		this.status = status;
		this.base64Img = base64Img;
	}
}

public class PreviewMemeServlet extends HttpServlet {

	private static final String IMAGE_ID_PARAM = "imageId";
	private static final String TEXT_PARAM = "text";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int status;
		String base64Img = null;
		try {
			// acquiring resources
			int imageId = Integer.valueOf(request.getParameter(IMAGE_ID_PARAM));
			String text = request.getParameter(TEXT_PARAM);

			HttpSession session = request.getSession();
			Room room = (Room) session.getAttribute(SessionAttributeKeys.ROOM);
			
			// transforming image
			BufferedImage image = room.getImages()[imageId];

			BufferedImage result = TextUtil.align(image, image.getWidth(), image.getHeight(),
					TextUtil.AlignmentX.CENTER, TextUtil.AlignmentY.MID, IMPACT_FONT, Color.WHITE, Color.BLACK,
					Color.DARK_GRAY, 3.5, text.toCharArray());
				
			// encoding result to base64
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(result, "PNG", out);
			byte[] bytes = out.toByteArray();
			
	        base64Img = Base64.getEncoder().encodeToString(bytes);
			status = 0;
		} catch (Exception e) {
			e.printStackTrace();
			status = 1;
		}

		// packaging results
		Gson gson = new Gson();
		String json = gson.toJson(new PreviewMemeResponse(status, base64Img));
		response.getWriter().append(json);
	}

}
