package ec.espe.edu.coffeeshop.utils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb://coffeeshop:coffeeshopMKA@157.137.223.54:27017/coffeeshop?authSource=coffeeshop";
    private static final String DATABASE_NAME = "coffeeshop";
    private static MongoClient mongoClient = null;
    private MongoDBConnection() {}
    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_STRING);
                System.out.println("Successfully connected to MongoDB.");
            } catch (Exception e) {
                System.err.println("Error connecting to MongoDB: " + e.getMessage());
            }
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
