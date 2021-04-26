package util;
 
import static resources.Registry.MONGO;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
 
@Deprecated
public class Util {
    private static final String DB = "Anything",COLLECTION = "LoginInfo";
    
	public static boolean findUser(String username, String email) {
        final MongoCollection<Document> col = MONGO.getDatabase(DB).getCollection(COLLECTION);
        return col.find(new BasicDBObject("name",username)).iterator().hasNext() ||
               col.find(new BasicDBObject("email",email)).iterator().hasNext();
    }
    
	public static boolean createUser(String username, String password, String email) {
        final MongoCollection<Document> col = MONGO.getDatabase(DB).getCollection(COLLECTION);
        if(col.find(new BasicDBObject("name",username)).iterator().hasNext() ||
           col.find(new BasicDBObject("email",email)).iterator().hasNext()) return false;
        
        col.insertOne(new Document("name",username)
                           .append("password",password)
                           .append("email",email));
        return true;
    }
}