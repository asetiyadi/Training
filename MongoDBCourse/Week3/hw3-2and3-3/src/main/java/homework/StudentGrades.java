package homework;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBList;
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
		DB db = client.getDB("school");
		DBCollection collection = db.getCollection("students");
		
		DBCursor cursor = collection.find();
		
		try {
			while(cursor.hasNext()) {
				DBObject student = cursor.next();
				BasicDBList scores = (BasicDBList) student.get("scores");
				BasicDBObject[] scoreArray = scores.toArray(new BasicDBObject[0]);
				
				System.out.println(student);
				
				BasicDBObject[] tempScoreArray = new BasicDBObject[scoreArray.length];
				BasicDBObject tempScore = null;
				double homeworkScore = -1;
				int x = 0;
				
				for(BasicDBObject score : scoreArray) {
					if(score.get("type").toString().equalsIgnoreCase("homework")) {
						System.out.println(score.get("type") + ": " + score.getDouble("score"));
						
						if(homeworkScore == -1) {
							homeworkScore = score.getDouble("score");
						}
						else if(homeworkScore < score.getDouble("score")){
							homeworkScore = score.getDouble("score");
						}
					}
					else {
						tempScore = new BasicDBObject();
						tempScore.append("type", score.get("type"));
						tempScore.append("score", score.get("score"));
						tempScoreArray[x] = tempScore;
						x++;
					}
				}
				
				tempScore = new BasicDBObject();
				tempScore.append("type", "homework");
				tempScore.append("score", homeworkScore);
				tempScoreArray[x] = tempScore;
				
				List<BasicDBObject> arrObject = new ArrayList<BasicDBObject>();
								
				for(int y=0; y<tempScoreArray.length; y++) {
					if(tempScoreArray[y] != null) {
						arrObject.add(tempScoreArray[y]);
					}
				}
				
				BasicDBObject targetStudent = new BasicDBObject("_id", student.get("_id"));
				BasicDBObject updatedScores = new BasicDBObject("$set", new BasicDBObject("scores", arrObject));
				
				System.out.println(targetStudent + ": " + updatedScores);
				System.out.println();
				collection.update(targetStudent, updatedScores);
			}
		}
		finally {
			cursor.close();
		}
	}

}
