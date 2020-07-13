package it.fabiano.bigdata.sparkstreming.es01;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;

import it.fabiano.bigdata.sparkstreming.utils.SentimentAnalyzer;
import scala.Tuple2;
import twitter4j.Status;

/**
* Java-Spark-Training-Course
*
* @author  Gaetano Fabiano
* @version 1.0.0
* @since   2019-07-19 
*/
public class TwitterSentiment  {

	final static String consumerKey = "eD1fATQ0VtXCYEc3toSM1Xgng";
	final static String consumerSecret = "KbAuly1xcly55lIB7CShl0JcK0saDHOci5cH1p2X9vV8HTvpcy";
	final static String accessToken = "841603218-5S1KTpS6gOOcTPNeJMDqm2vtDRqGWGrX1aQfx2xM";
	final static String accessTokenSecret = "YnUnqen0Oy024gyyCQ8h8wloirZi2mxgKdLz9g1UhQ7oE";

	public static void main(String[] args) throws InterruptedException  {

		//Logger
		Logger.getLogger("org").setLevel(Level.ERROR);

		
		String[] filters = {"trump"};



		System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
		System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
		System.setProperty("twitter4j.oauth.accessToken", accessToken);
		System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);

		SparkConf sparkConf = new SparkConf().setAppName("JavaTwitterHashTagJoinSentiments").setMaster("local[*]");
		//SparkConf sparkConf = new SparkConf().setAppName("JavaTwitterHashTagJoinSentiments");
		
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));

		JavaReceiverInputDStream<Status> stream = TwitterUtils.createStream(jssc, filters);
		
		
		JavaDStream<String> tweets = stream.map(status -> status.getText());
	
		
		
		JavaPairDStream<String, String> tweetWithScoreDStream =
				tweets.mapToPair(tweetText -> 
				new Tuple2<>(
						"TWEET: "+tweetText, 
						"SENTIMENT: "+Double.valueOf(SentimentAnalyzer.calculateWeightedSentimentScore(tweetText))));

		
		JavaPairDStream<String, String> tweetWithSentiment =
				tweets.mapToPair(tweetText -> 
				new Tuple2<>(
						"TWEET: "+tweetText, 
						"SENTIMENT: "+SentimentAnalyzer.getSimplifiedSentiment(tweetText)));
		
		tweetWithScoreDStream.print();

		tweetWithSentiment.print();
		jssc.start();
		jssc.awaitTermination();
		
	}

	

}
