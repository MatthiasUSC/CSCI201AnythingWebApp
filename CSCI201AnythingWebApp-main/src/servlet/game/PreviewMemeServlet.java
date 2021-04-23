package servlet.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import game.Room;
import image.ImageHistory;
import image.TextUtil.AlignmentX;
import image.TextUtil.AlignmentY;
import sessionAttributes.SessionAttributeKeys;

/*class PreviewMemeResponse {
    public final int status;
    public final String base64Img;
    
    public PreviewMemeResponse(int status,String base64Img) { this.status = status; this.base64Img = base64Img; }
}*/

@WebServlet("/PreviewMemeServlet")
public class PreviewMemeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String IMAGE_ID_PARAM = "imageId";
    private static final String TEXT_PARAM = "text";
    private static final Encoder B64 = Base64.getEncoder();
    
    protected void doPost(final HttpServletRequest request,final HttpServletResponse response) throws ServletException,
                                                                                               IOException {
        int status;
        String base64Img = null;
        try {
            // acquiring resources
            int imageId = Integer.parseInt(request.getParameter(IMAGE_ID_PARAM));
            String text = request.getParameter(TEXT_PARAM);
            
            HttpSession session = request.getSession();
            Room room = (Room)session.getAttribute(SessionAttributeKeys.Room.toString());
            
            // transforming image
            // TODO get when round starts and convert to ImageHistory
            // BufferedImage image = room.getImages()[imageId];
            
            /*
             * BufferedImage result = TextUtil.align(image, image.getWidth(),
             * image.getHeight(), TextUtil.AlignmentX.CENTER, TextUtil.AlignmentY.MID,
             * IMPACT_FONT, Color.WHITE, Color.BLACK, Color.DARK_GRAY, 3.5,
             * text.toCharArray());
             */
            
            // encoding result to base64
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // ImageIO.write(result, "PNG", out);
            // TODO get args from the user.
            ImageIO.write(pushState(
                null,
                null,
                -1,
                -1,
                null,
                null,
                null,
                null,
                null,
                -1,
                (ImageHistory)session.getAttribute(SessionAttributeKeys.Image.toString()),
                null),"PNG",out);
            // byte[] bytes = out.toByteArray();
            
            // base64Img = Base64.getEncoder().encodeToString(bytes);
            // status = 0;
            response.getWriter().append("{\"status\":0,\"base64Img\":\"")
                                .append(B64.encodeToString(out.toByteArray()))
                                .append("\"}");
        } catch(Exception e) {
            e.printStackTrace();
            //status = 1;
        }
        
        // packaging results
        //Gson gson = new Gson();
        //String json = gson.toJson(new PreviewMemeResponse(status,base64Img));
        response.getWriter().append("{\"status\":1,\"base64Img\":null}");
    }
    
    private static BufferedImage pushState(final AlignmentY y,final AlignmentX x,final int size,
                                           final double outlineSize,final Color fill,final Color outline,
                                           final Color bgH,final Color bgF,final Font f,final int style,
                                           final ImageHistory ih,final char...text) {
        ih.pushState(y,x,size,outlineSize,fill,outline,bgH,bgF,f,style,text);
        return ih.getImage();
    }
}
