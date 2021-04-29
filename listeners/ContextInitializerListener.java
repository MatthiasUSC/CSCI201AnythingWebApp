package listeners;

import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextInitializerListener implements ServletContextListener {
    /*public void contextInitialized(final ServletContextEvent sce) {
        try {Class.forName("com.mongodb.MongoClientURI");}
        catch(final ClassNotFoundException e) {e.printStackTrace(); System.exit(1);}
    }*/
	
    /*@Override
    public void contextDestroyed(final ServletContextEvent sce) {
        //Registry.MONGO.close();
    }*/
}