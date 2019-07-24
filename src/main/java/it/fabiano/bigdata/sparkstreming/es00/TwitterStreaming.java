package it.fabiano.bigdata.sparkstreming.es00;


import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;

import twitter4j.Status;
/**
* Java-Spark-Training-Course
*
* @author  Gaetano Fabiano
* @version 1.0.0
* @since   2019-07-19 
*/

public class TwitterStreaming {
	
	
	//Credentials
	final static String consumerKey = "";
	final static String consumerSecret = "";
	final static String accessToken = "";
	final static String accessTokenSecret = "";


	public static void main(String[] args) throws InterruptedException {


		//Logger less verbose
		Logger.getLogger("org").setLevel(Level.ERROR);

		
		SparkConf sparkConf = new SparkConf().setAppName("TwitterStreaming").setMaster("local[*]");

		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));


		// can use them to generate OAuth credentials
		System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
		System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
		System.setProperty("twitter4j.oauth.accessToken", accessToken);
		System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);

		
		
		String[] filters = {"trump"};
		
	

		
		
		//Stream
		JavaReceiverInputDStream<Status> stream = TwitterUtils.createStream(jssc, filters);


		//take words from twitter just to try the flatMap
		JavaDStream<String> words = stream.flatMap( status -> Arrays.asList(status.getText().split(" ")).iterator() );
		

		//Try map functions here
		JavaDStream<Object> user = stream.map(status -> status.getUser());

		words.print();
		user.print();
		
		jssc.start();
		jssc.awaitTermination();
	}
}