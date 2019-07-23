package it.fabiano.bigdata.sparkstreming.es00;


import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalog.Function;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;

import scala.Tuple2;
import twitter4j.Status;
/**
* Java-Spark-Training-Course
*
* @author  Gaetano Fabiano
* @version 1.0.0
* @since   2019-07-19 
*/

public class TwitterStreaming {
	final static String consumerKey = "";
	final static String consumerSecret = "";
	final static String accessToken = "";
	final static String accessTokenSecret = "";


	public static void main(String[] args) throws InterruptedException {


		//Logger
		Logger.getLogger("org").setLevel(Level.ERROR);

		//Consumer API keys
		String[] filters = {"trump"};


		// can use them to generate OAuth credentials
		System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
		System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
		System.setProperty("twitter4j.oauth.accessToken", accessToken);
		System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);

		SparkConf sparkConf = new SparkConf().setAppName("JavaTwitterHashTagJoinSentiments").setMaster("local[*]");

		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));

		JavaReceiverInputDStream<Status> stream = TwitterUtils.createStream(jssc, filters);


		//take words from twitter
		JavaDStream<String> words = stream.flatMap( status -> Arrays.asList(status.getText().split(" ")).iterator() );
		
		words.print();

		
	

		jssc.start();
		jssc.awaitTermination();
	}
}