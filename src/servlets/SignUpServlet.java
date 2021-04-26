package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.MongoDBUtil;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String ERR_NAME = "Please enter username and password.";
    private static final String ERR_DUPE = "An account with these credentials already exist.";
    private static final String SUCCESS = "Success!";
    
    @Override
    public void doPost(final HttpServletRequest request,
                       final HttpServletResponse response)
                       throws IOException,ServletException {
        final String param1 = request.getParameter(RequestCode.ID.s),
                     param2 = param1 == null || param1.isEmpty()? null : request.getParameter(RequestCode.PWD.s),
                     param3 = param2 == null || param2.isEmpty()? null : request.getParameter(RequestCode.EMAIL.s);
        final PrintWriter pw = response.getWriter();
        if(param3 == null || param3.isBlank()) pw.print(ERR_NAME);
        else if(!MongoDBUtil.createUser(param1,param2,param3)) pw.print(ERR_DUPE);
        else pw.print(SUCCESS);
    }
}