package com.jcg.mongodb.servlet;
 
import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        handleRequest(req, response);
    }
 
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
 
        // Reading post parameters from the request
        String param1 = req.getParameter("login_id"), 
                param2 = req.getParameter("login_pwd"),
        		 param3 = req.getParameter("email");
        System.out.println(param1 + param2 + param3);
        // Checking for null and empty values
        if(param1 == null || param2 == null || param3 == null || "".equals(param1) || "".equals(param2) || "".equals(param3)) {
            req.setAttribute("error_message", "Please enter username and password.");
            req.getRequestDispatcher("/whatever the html is").forward(req, resp);
        } else {
            boolean isUserFound = Util.createUser(param1, param2, param3);
            if(isUserFound) {         
            	resp.getWriter().print("Success!");
            	//req.getRequestDispatcher("/whatever the html is").forward(req, resp);
            } else {
            	resp.getWriter().print("An account with these credentials already exist.");
            	req.setAttribute("error_message", "An account with these credentials already exist.");
            }   
        }       
    }
}