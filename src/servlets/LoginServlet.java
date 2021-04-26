package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.MongoDBUtil;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final String ERR_NAME = "Please enter username and password.";
    static final String ERR_NONE = "Invalid credentials. Please try again.";
    static final String ERR_NONE_ATTR = "Invalid credentials.";
    private static final String SUCCESS = "Success!";
    private static final long serialVersionUID = 1L;
    
    public void doPost(final HttpServletRequest request,
                       final HttpServletResponse response)
                       throws IOException,ServletException {
        final String param1 = request.getParameter(RequestCode.ID.s),
                     param2 = param1 == null || param1.isBlank()? null : request.getParameter(RequestCode.PWD.s);
        final PrintWriter pw = response.getWriter();
        if(param2 == null || param2.isBlank()) pw.append(ERR_NAME);
        else if(!MongoDBUtil.findUser(param1,param2)) pw.append(ERR_NONE);
        else pw.append(SUCCESS);
    }
}