package it.fabiano.bigdata.sparkstreming.es01;

public class TwitterConfiguration {
	//Credentials
	public final static String consumerKey = "";
	public final static String consumerSecret = "";
	public final static String accessToken = "";
	public final static String accessTokenSecret = "";
	
	static {
		System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
		System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
		System.setProperty("twitter4j.oauth.accessToken", accessToken);
		System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);
	}
}
