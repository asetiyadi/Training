package com.tengen;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class StudentGrades {

	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
		DB db = client.getDB("students");
		DBCollection collection = db.getCollection("grades");
		
		DBObject query = new BasicDBObject("type", "homework");
		
		DBCursor cursor = collection.find(query,
				new BasicDBObject("id", true)
				.append("student_id", true)
				.append("score", true))
				.sort(new BasicDBObject("student_id", 1));
		
		try {
			DBObject currentDoc = null;
			
			while(cursor.hasNext()) {
				if(currentDoc == null) {
					currentDoc = cursor.next();
				}
				
				System.out.println("PREVIOUS: " + currentDoc);
				
				DBObject nextRecord = cursor.next();
				System.out.println("NEXT: " + nextRecord);
				
				if(currentDoc.get("student_id") == nextRecord.get("student_id")) {
					System.out.println("Prev studentId: " + currentDoc.get("student_id") + " | nextId: " + nextRecord.get("student_id"));
					if(Double.parseDouble(currentDoc.get("score").toString()) < Double.parseDouble(nextRecord.get("score").toString())) {
						System.out.println("Prev score " + currentDoc.get("score") + " is LOWER then next score " + nextRecord.get("score"));
						collection.remove(new BasicDBObject("_id", currentDoc.get("_id")));
						currentDoc = nextRecord;
					}
					else {
						System.out.println("Prev score " + currentDoc.get("score") + " is HIGHER then next score " + nextRecord.get("score"));
						collection.remove(new BasicDBObject("_id", nextRecord.get("_id")));
					}
				}
				else {
					currentDoc = nextRecord;
				}
				
				System.out.println();
			}
		}
		finally {
			cursor.close();
		}
		
		System.out.println(collection.getCount());
	}

}
