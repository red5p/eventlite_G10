package uk.ac.man.cs.eventlite.config;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import Geocoding.twitterImplementation;

import twitter4j.Status;

public class SendTweetTest {
	
	@Autowired
	@InjectMocks
	private twitterImplementation twitterImplementation;

	
	@BeforeEach
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	

	@Test
    void timelineTest() throws Exception {
		
		List<Status> temp = twitterImplementation.getTimeline();
		
		assertTrue(0 < temp.size());
	}
	
	@Test
    void postTweetTest() throws Exception {
		
		String myMessage = "test: " + new Date() + ", " + new Random().nextInt();
		
		Status tweet = twitterImplementation.postTweet(myMessage);
		List<Status> tweets = twitterImplementation.getTimeline();
		
		String msg = tweets.get(0).getText();
		
		assertEquals(myMessage, msg);
		assertNotEquals("not the test tweet", msg);
	}
	
}
