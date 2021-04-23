//package com.jcg.mongodb.servlet;
package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Util;

import static servlets.REGISTRY.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final String TARGET = "/whatever the html is"; //TODO
    static final String ERR_NONE = "Invalid credentials. Please try again.";
    static final String ERR_NONE_ATTR = "Invalid credentials.";
    private static final long serialVersionUID = 1L;
    
    public void doPost(HttpServletRequest req,HttpServletResponse response) throws IOException,ServletException {
        // TODO do we need this extra function call?
        handleRequest(req,response);
    }
    
    public void handleRequest(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException {
        
        // Reading post parameters from the request
        String param1 = req.getParameter(RequestCode.ID.s),
               param2 = param1 == null || param1.isBlank()? null : req.getParameter(RequestCode.PWD.s);
        
        // Checking for null and empty values
        if(param2 == null || param2.isBlank()) {
            req.setAttribute(ERR_ATTR,ERR_NAME);
            req.getRequestDispatcher(TARGET).forward(req,resp);
        } else {
            boolean isUserFound = Util.findUser(param1,param2);
            if(isUserFound) {
                resp.getWriter().print(SUCCESS);
                // req.getRequestDispatcher(TARGET).forward(req, resp);
            } else {
                resp.getWriter().print(ERR_NONE);
                req.setAttribute(ERR_ATTR,ERR_NONE_ATTR);
                // req.getRequestDispatcher(TARGET).forward(req, resp);
            }
        }
    }
}