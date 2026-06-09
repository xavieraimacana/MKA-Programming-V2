package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Singleton class to manage the MongoDB client and connection pool.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class MongoDBConnection {
    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongoDBConnection() {
        // En producción, estos valores deben leerse de variables de entorno o archivos de configuración (.properties)
        String connectionString = System.getenv("MONGODB_URI");
        if (connectionString == null || connectionString.isEmpty()) {
            connectionString = "mongodb://localhost:27017";
        }
        
        String databaseName = "coffeeshop";
        try {
            this.mongoClient = MongoClients.create(connectionString);
            this.database = mongoClient.getDatabase(databaseName);
            System.out.println("Conectado con éxito a MongoDB, Base de Datos: " + databaseName);
        } catch (Exception e) {
            System.err.println("Error de conexión con MongoDB: " + e.getMessage());
        }
    }

    /**
     * Retrieves the single instance of MongoDBConnection.
     */
    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }

    /**
     * Gets the MongoDatabase instance.
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Closes the MongoDB client connection.
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión con MongoDB cerrada.");
        }
    }
}
