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

import game.Player;
import image.TextUtil.AlignmentX;
import image.TextUtil.AlignmentY;
import sessionAttributes.SessionAttributeKeys;

@WebServlet("/PreviewMemeServlet")
public class PreviewMemeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Encoder B64 = Base64.getEncoder();
    
    private static interface getter<T> {T get(final String r);}
    private static enum EditArg {
        alignmentY(r -> AlignmentY.valueOf(r)),
        alignmentX(r -> AlignmentX.valueOf(r)),
        size(r -> Integer.parseInt(r)),
        outlineSize(r -> Double.parseDouble(r)),
        fillColor(r -> Color.getColor(r)),
        outlineColor(r -> Color.getColor(r)),
        headerBGColor(r -> Color.getColor(r)),
        footerBGColor(r -> Color.getColor(r)),
        font(r -> resources.Registry.FONTS.getOrDefault(r,resources.Registry.IMPACT_FONT)),
        fontStyle(r -> resources.Registry.STYLES.getOrDefault(r,Font.PLAIN)),
        text(r -> r.toCharArray());
        
        final getter<?> g;
        private <T> EditArg(final getter<T> g) {this.g = g;}
        @SuppressWarnings("unchecked")
        private <T> T get(final HttpServletRequest r) {return (T)g.get(r.getParameter(toString()));}
    }
    
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
                          throws ServletException,IOException {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(pushState(request,(Player)request.getSession().getAttribute(SessionAttributeKeys.Player.toString())),"PNG",out);
            response.getWriter().append("{\"status\":0,\"base64Img\":\"")
                                .append(B64.encodeToString(out.toByteArray()))
                                .append("\"}");
            return;
        } catch(final Exception e) {e.printStackTrace();}
        response.getWriter().append("{\"status\":1,\"base64Img\":null}");
    }
    private static BufferedImage pushState(final HttpServletRequest r,final Player p) {
        return p.updateImage(
            EditArg.alignmentY.get(r),
            EditArg.alignmentX.get(r),
            EditArg.size.get(r),
            EditArg.outlineSize.get(r),
            EditArg.fillColor.get(r),
            EditArg.outlineColor.get(r),
            EditArg.headerBGColor.get(r),
            EditArg.footerBGColor.get(r),
            EditArg.font.get(r),
            EditArg.fontStyle.get(r),
            EditArg.text.get(r)
        );
    }
}
