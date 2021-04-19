package com.jcg.mongodb.servlet;
 
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
 
public class Util {
 
    // Method to search a user in the mongodb
    @SuppressWarnings("deprecation")
	public static boolean findUser(String username, String email) {
    	MongoClient mongo = new MongoClient("localhost", 27017);
        String db_name = "Anything",
                collection = "LoginInfo";
 
        DB db = mongo.getDB(db_name);
 
        DBCollection col = db.getCollection(collection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("name", username);

        DBCursor cursor = col.find(searchQuery);

        while (cursor.hasNext()) {
        	mongo.close();
            return true;
        }
        
        searchQuery = new BasicDBObject();
        searchQuery.put("email", email);
        
        cursor = col.find(searchQuery);

        while (cursor.hasNext()) {
        	mongo.close();
            return true;
        }
        
        mongo.close();
        return false;
    }
    
    @SuppressWarnings("deprecation")
	public static boolean createUser(String username, String password, String email) {
    	MongoClient mongo = new MongoClient("localhost", 27017);
        String db_name = "Anything",
                collection = "LoginInfo";
 
        DB db = mongo.getDB(db_name);
 
        DBCollection col = db.getCollection(collection);

        if(findUser(username, password)) {
        	mongo.close();
        	return false;
        }
        
        BasicDBObject document = new BasicDBObject();
        document.put("name", username);
        document.put("password", password);
        document.put("email", email);
        
        col.insert(document);
        
        mongo.close();
        return true;
    }
}