package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import resources.Registry;

@WebListener
public class ContextInitializerListener implements ServletContextListener {
    /*public void contextInitialized(final ServletContextEvent sce) {}*/
	
    /*@Override
    public void contextDestroyed(final ServletContextEvent sce) {
        Registry.MONGO.close();
    }*/
}
