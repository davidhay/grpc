package com.ealanta.grpc.blog.server;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

  public static final String KEY_AUTHOR_ID = "authorId";
  public static final String KEY_TITLE = "title";
  public static final String KEY_ID = "_id";
  public static final String KEY_CONTENT = "content";

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

  MongoCollection<Document> collection = database.getCollection("blog");


  @Override
  public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {

    System.out.println("Received Create Blog request");

    Blog blog = request.getBlog();

    Document doc = new Document(KEY_AUTHOR_ID, blog.getAuthorId())
        .append(KEY_TITLE, blog.getTitle())
        .append(KEY_CONTENT, blog.getContent());

    System.out.println("Inserting blog...");
    // we insert (create) the document in mongoDB
    collection.insertOne(doc);


    // we retrieve the MongoDB generated ID
    String id = doc.getObjectId(KEY_ID).toString();
    System.out.println("Inserted blog: " + id);

    CreateBlogResponse response = CreateBlogResponse.newBuilder()
        .setBlog(blog.toBuilder().setId(id).build())
        .build();

    responseObserver.onNext(response);

    responseObserver.onCompleted();

  }


  @Override
  public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
    System.out.println("Received Read Blog request");

    String blogId = request.getBlogId();

    System.out.println("Searching for a blog");
    Document result = null;

    try {
      result = collection.find(eq(KEY_ID, new ObjectId(blogId)))
          .first();
    } catch (Exception e) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The blog with the corresponding id was not found")
              .augmentDescription(e.getLocalizedMessage())
              .asRuntimeException()
      );
    }

    if (result == null) {
      System.out.println("Blog not found");
      // we don't have a match
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The blog with the corresponding id was not found")
              .asRuntimeException()
      );
    } else {
      System.out.println("Blog found, sending response");
      Blog blog = documentToBlog(result);

      responseObserver.onNext(ReadBlogResponse.newBuilder().setBlog(blog).build());

      responseObserver.onCompleted();
    }

  }

  @Override
  public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {
    System.out.println("Received Update Blog request");

    Blog blog = request.getBlog();

    String blogId = blog.getId();

    System.out.println("Searching for a blog so we can update it");
    Document result = null;

    try {
      result = collection.find(eq(KEY_ID, new ObjectId(blogId)))
          .first();
    } catch (Exception e) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The blog with the corresponding id was not found")
              .augmentDescription(e.getLocalizedMessage())
              .asRuntimeException()
      );
    }

    if (result == null) {
      System.out.println("Blog not found");
      // we don't have a match
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The blog with the corresponding id was not found")
              .asRuntimeException()
      );
    } else {
      Document replacement = new Document(KEY_AUTHOR_ID, blog.getAuthorId())
          .append(KEY_TITLE, blog.getTitle())
          .append(KEY_CONTENT, blog.getContent())
          .append(KEY_ID, new ObjectId(blogId));

      System.out.println("Replacing blog in database...");

      collection.replaceOne(eq(KEY_ID, result.getObjectId(KEY_ID)), replacement);

      System.out.println("Replaced! Sending as a response");
      responseObserver.onNext(
          UpdateBlogResponse.newBuilder()
              .setBlog(documentToBlog(replacement))
              .build()
      );

      responseObserver.onCompleted();
    }
  }

  private Blog documentToBlog(Document document){
    return Blog.newBuilder()
        .setAuthorId(document.getString(KEY_AUTHOR_ID))
        .setTitle(document.getString(KEY_TITLE))
        .setContent(document.getString(KEY_CONTENT))
        .setId(document.getObjectId(KEY_ID).toString())
        .build();
  }

  @Override
  public void deleteBlog(DeleteBlogRequest request, StreamObserver<DeleteBlogResponse> responseObserver) {
    System.out.println("Received Delete Blog Request");

    String blogId = request.getBlogId();
    DeleteResult result = null;
    try {
      result = collection.deleteOne(eq(KEY_ID, new ObjectId(blogId)));
    } catch (Exception e) {
      System.out.println("Blog not found");
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The blog with the corresponding id was not found")
              .augmentDescription(e.getLocalizedMessage())
              .asRuntimeException()
      );
    }

    if (result.getDeletedCount() == 0) {
      System.out.println("Blog not found");
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("The blog with the corresponding id was not found")
              .asRuntimeException()
      );
    } else {
      System.out.println("Blog was deleted");
      responseObserver.onNext(DeleteBlogResponse.newBuilder()
          .setBlogId(blogId)
          .build());

      responseObserver.onCompleted();
    }

  }

  @Override
  public void listBlog(ListBlogRequest request, StreamObserver<ListBlogResponse> responseObserver) {
    System.out.println("Received List Blog Request");

    collection.find().iterator().forEachRemaining(document -> responseObserver.onNext(
        ListBlogResponse.newBuilder().setBlog(documentToBlog(document)).build()
    ));

    responseObserver.onCompleted();
  }
}

