package com.ealanta.grpc.blog;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

public class MongoConnectTest {

  @Test
  void test(){
    String password = System.getenv("PASSWORD");
    String conn = String.format("mongodb+srv://dhadmin:%s@cluster0.qkj38.mongodb.net/myFirstDatabase?retryWrites=true&w=majority",password);
    ConnectionString connectionString = new ConnectionString(conn);
    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .serverApi(ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build())
        .build();
    MongoClient mongoClient = MongoClients.create(settings);
    MongoDatabase database = mongoClient.getDatabase("grpc");

    MongoCollection<Document> blog = database.getCollection("blog");

    System.out.printf("grpc.blog COUNT [%d]%n",blog.countDocuments());
    Bson filter = new Document().append("_id", new ObjectId("62164ec17134a4e48bd31341"));

    FindIterable<Document> result = blog.find(filter);

    result.forEach( doc -> {
      System.out.println("GOT DOC");
      System.out.println(doc);
    });

    System.out.println("FIN");

  }
}
