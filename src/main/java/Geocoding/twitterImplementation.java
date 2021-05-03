package Geocoding;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.TwitterException;

import twitter4j.TwitterException;


public class twitterImplementation {

	private Twitter twitter;
	
//	public twitterImplementation() {
//	  ConfigurationBuilder cb = new ConfigurationBuilder(); 
//	  cb.setDebugEnabled(true) 
//	  .setOAuthConsumerKey("qcLeIVndOJEsZqKdTNfCTHLBP") 
//	  .setOAuthConsumerSecret("r2NrMlDpUea3MOh5R435BKFb3rZbPg2OBJPQ1ppjkcMoXDx3l2") 
//	  .setOAuthAccessToken("1384866829059907586-tHmGeXihiKc5AGbTfQjuvaY7E3Ay4I") 
//	  .setOAuthAccessTokenSecret("F8OlLEmhAKFnxbWGNrd8SEoyv1tQSJYI3K6TJtscHhkot");
//	  TwitterFactory tf = new TwitterFactory(cb.build()); 
//	  twitter = tf.getInstance();
//	}

	public twitterImplementation() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("hSlxrddPqsW0KuY9SFKIvlc7o")
				.setOAuthConsumerSecret("r8orgE1qpZIzwgjxhGGKNcyjBU98Zfvw44IAH6x4Ca7I5HcmbR")
				.setOAuthAccessToken("1384866829059907586-4GeUzV6OYZ3JjrriGDX2vhyXOtDdeU")
				.setOAuthAccessTokenSecret("RDPfWOzMVjrHCMGg2FNqMXlQ3pxBpGuDr9WDNHyfMr1en");
		TwitterFactory tf = new TwitterFactory(cb.build());
		this.twitter = tf.getInstance();
	}

	public List<Status> getTimeline() throws TwitterException {
		List<Status> res = twitter.getHomeTimeline();
		return res;
	}

	public Status postTweet(String tweet) throws TwitterException{
		return twitter.updateStatus(tweet);
	}

}
