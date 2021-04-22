package com.jcg.mongodb.servlet;
 
import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        handleRequest(req, response);
    }
 
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
 
        // Reading post parameters from the request
        String param1 = req.getParameter("login_id"), 
                param2 = req.getParameter("login_pwd");

        // Checking for null and empty values
        if(param1 == null || param2 == null || "".equals(param1) || "".equals(param2)) {
            req.setAttribute("error_message", "Please enter username and password.");
            req.getRequestDispatcher("/whatever the html is").forward(req, resp);
        } else {
            boolean isUserFound = Util.findUser(param1, param2);
            if(isUserFound) {  
            	resp.getWriter().print("Success!");
                //req.getRequestDispatcher("/whatever the html is").forward(req, resp);
            } else {
            	resp.getWriter().print("Invalid credentials. Please try again.");
                req.setAttribute("error_message", "Invalid credentials.");
                //req.getRequestDispatcher("/whatever the html is").forward(req, resp);
            }   
        }       
    }
}