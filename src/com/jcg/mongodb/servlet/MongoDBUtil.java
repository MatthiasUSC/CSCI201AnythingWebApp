package com.jcg.mongodb.servlet;
 
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
 
public class MongoDBUtil {
 
    // Method to search a user in the mongodb
	public static boolean findUser(String username, String password) {
    	MongoClientURI uri = new MongoClientURI(
    		    "mongodb+srv://mikelism:mikelism@cluster0.0gnzp.mongodb.net/Anything?retryWrites=true&w=majority");
    	
    	MongoClient mongo = new MongoClient(uri);
        String db_name = "Anything",
                collection = "LoginInfo";
        
        MongoDatabase db = mongo.getDatabase(db_name);
 
        FindIterable<Document> col = db.getCollection(collection).find();
        MongoCursor<Document> iterator = col.iterator();
        int foundCount = 0;//if its 2, credentials matched
        while (iterator.hasNext()) {
        	foundCount = 0;
            Document doc = iterator.next();
            String name = doc.getString("name");
            if(name.equals(username)) {
            	foundCount++;
            }
            String pwd = doc.getString("password");
            if(pwd.equals(password)) {
            	foundCount++;
            }
            if(foundCount == 2) {
            	mongo.close();
                return true;
            }
        }
        
        mongo.close();
        return false;
    }
    
	public static boolean createUser(String username, String password, String email) {
    	MongoClientURI uri = new MongoClientURI(
    		    "mongodb+srv://mikelism:mikelism@cluster0.0gnzp.mongodb.net/Anything?retryWrites=true&w=majority");
    	
    	MongoClient mongo = new MongoClient(uri);
        String db_name = "Anything",
                collection = "LoginInfo";
 
        MongoDatabase db = mongo.getDatabase(db_name);

        if(findUser(username, password)) {
        	mongo.close();
        	return false;
        }

        Document doc = new Document();
        doc.put("name", username);
        doc.put("password", password);
        doc.put("email", email);
        
        db.getCollection(collection).insertOne(doc);
        
        mongo.close();
        return true;
    }
}