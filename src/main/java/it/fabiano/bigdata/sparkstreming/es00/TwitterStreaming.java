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

import it.fabiano.bigdata.sparkstreming.es01.TwitterConfiguration;
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

	

	public static void main(final String[] args) throws InterruptedException {



		//Logger less verbose
		Logger.getLogger("org").setLevel(Level.ERROR);


		final SparkConf sparkConf = new SparkConf().setAppName("TwitterStreaming").setMaster("local[*]");

		final JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));


		// can use them to generate OAuth credentials
		System.setProperty("twitter4j.oauth.consumerKey", TwitterConfiguration.consumerKey);
		System.setProperty("twitter4j.oauth.consumerSecret", TwitterConfiguration.consumerSecret);
		System.setProperty("twitter4j.oauth.accessToken", TwitterConfiguration.accessToken);
		System.setProperty("twitter4j.oauth.accessTokenSecret", TwitterConfiguration.accessTokenSecret);



		final String[] filters = {"conte"};

	

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