package com.tengen;

import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HelloWorldMongoDBSparkFreemarkerStyle {

	public static void main(String[] args) throws UnknownHostException {
		final Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(HelloWorldMongoDBSparkFreemarkerStyle.class, "/");
		
		MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
		
		DB database = client.getDB("m101");
		final DBCollection collection = database.getCollection("hw1");
		
		Spark.get("/hello", new Route() {
			
			public Object handle(Request arg0, Response arg1) throws Exception {
				StringWriter writer = new StringWriter();
				
				try {
					Template helloTemplate = configuration.getTemplate("hello.ftl");
					
					DBObject document = collection.findOne();
					
					helloTemplate.process(document, writer);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (TemplateException e) {
					e.printStackTrace();
				}
				
				return writer;
			}
		});
	}
	
}
