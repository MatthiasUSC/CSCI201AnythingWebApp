package listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import contextAttributes.ContextAttributeKeys;
import contextAttributes.GameManager;

/**
 * Application Lifecycle Listener implementation class ContextInitializerListener
 *
 */
@WebListener
public class ContextInitializerListener implements ServletContextListener {
	/**
     * Initializer of important global data
     */
	//TODO have data from a config/constants file be loaded here?
	//TODO also gotta look up whether it is better to have single constant file, or should constants be declared only in the classes they are needed in
    public void contextInitialized(ServletContextEvent sce)  { 
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute(ContextAttributeKeys.GAME_MANAGER, new GameManager());
    }
	
}
