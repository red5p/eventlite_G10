package Geocoding;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class twitterImplementation {

	private Twitter twitter;
	
	public twitterImplementation() {
	  ConfigurationBuilder cb = new ConfigurationBuilder(); 
	  cb.setDebugEnabled(true) 
	  .setOAuthConsumerKey("qcLeIVndOJEsZqKdTNfCTHLBP") 
	  .setOAuthConsumerSecret("r2NrMlDpUea3MOh5R435BKFb3rZbPg2OBJPQ1ppjkcMoXDx3l2") 
	  .setOAuthAccessToken("1384866829059907586-tHmGeXihiKc5AGbTfQjuvaY7E3Ay4I") 
	  .setOAuthAccessTokenSecret("F8OlLEmhAKFnxbWGNrd8SEoyv1tQSJYI3K6TJtscHhkot");
	  TwitterFactory tf = new TwitterFactory(cb.build()); 
	  twitter = tf.getInstance();
	}
}
