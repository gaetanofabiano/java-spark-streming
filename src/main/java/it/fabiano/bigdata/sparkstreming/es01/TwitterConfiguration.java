package it.fabiano.bigdata.sparkstreming.es01;

public class TwitterConfiguration {
	//Credentials
	public final static String consumerKey = "eD1fATQ0VtXCYEc3toSM1Xgng";
	public final static String consumerSecret = "KbAuly1xcly55lIB7CShl0JcK0saDHOci5cH1p2X9vV8HTvpcy";
	public final static String accessToken = "841603218-5S1KTpS6gOOcTPNeJMDqm2vtDRqGWGrX1aQfx2xM";
	public final static String accessTokenSecret = "YnUnqen0Oy024gyyCQ8h8wloirZi2mxgKdLz9g1UhQ7oE";
	
	static {
		System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
		System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
		System.setProperty("twitter4j.oauth.accessToken", accessToken);
		System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);
	}
}
