// package com.jcg.mongodb.servlet;
package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Util;

import static servlets.REGISTRY.*;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
    private static final String TARGET = "/whatever the html is"; //TODO
    
    private static final long serialVersionUID = 1L;
    
    public void doPost(HttpServletRequest req,HttpServletResponse response) throws IOException,ServletException {
        //TODO do we need this extra function call?
        handleRequest(req,response);
    }
    
    public void handleRequest(final HttpServletRequest req,
                              final HttpServletResponse resp)
                              throws IOException,ServletException {
        // Reading post parameters from the request
        String param1 = req.getParameter(RequestCode.ID.s),
               param2 = param1 == null || param1.isEmpty()? null : req.getParameter(RequestCode.PWD.s),
               param3 = param2 == null || param2.isEmpty()? null : req.getParameter(RequestCode.EMAIL.s);
        // Checking for null and empty values
        if(param3 == null || param3.isBlank()) {
            req.setAttribute(ERR_ATTR,ERR_NAME);
            req.getRequestDispatcher(TARGET).forward(req,resp);
        } else if(Util.createUser(param1,param2,param3)) {
            resp.getWriter().print(SUCCESS);
            // req.getRequestDispatcher(TARGET).forward(req, resp);
        } else {
            resp.getWriter().print(ERR_DUPE);
            req.setAttribute(ERR_ATTR,ERR_DUPE);
        }
    }
}