
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ArrayList<LoginInfo> loginEntries = new ArrayList<LoginInfo>();
	static {
		// Init test entires
		loginEntries.add(new LoginInfo("root", "root"));
		loginEntries.add(new LoginInfo("user1", "pass1"));
	}

	@Override
	public void init() throws ServletException {
		// Do required initialization
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String n = request.getParameter("username");
		String p = request.getParameter("userpass");
		LoginInfo login = new LoginInfo(n, p);

		if (isValidLogin(login)) {
			out.print("Correct username and password!");
			RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.include(request, response);
			/*
			 * // Enables new servlet to send to new page or something RequestDispatcher
			 * rd=request.getRequestDispatcher("servlet2"); rd.forward(request,response);
			 */
		} else {
			// Sends back to index.html
			out.print("Sorry username or password is incorrect.");
			RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.include(request, response);
		}
		out.close();
		doGet(request, response);
	}

	// TODO make this function interact with a MongoDB database
	private static boolean isValidLogin(LoginInfo login) {
		return loginEntries.contains(login);
	}

}
