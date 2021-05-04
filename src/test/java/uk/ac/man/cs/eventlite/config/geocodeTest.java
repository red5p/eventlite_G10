package uk.ac.man.cs.eventlite.config;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import Geocoding.Geocode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import uk.ac.man.cs.eventlite.entities.Venue;

public class geocodeTest {
	
	@Test
	public void venueWithInvalidAddress() {
		Venue venue = new Venue();
		venue.setId(2);
		venue.setName("Venue Name");
		venue.setCapacity(100);
		venue.setRoadName("ksksak");
		venue.setPostcode("jdssadskdsk");
		Double lat = venue.getLatitude();
		Double lon = venue.getLongitude();
		assertNull(lat);
		assertNull(lon);
	}
	
	@Test
	public void venueWithValidAddress() {
		Venue venue = new Venue();
		venue.setId(2);
		venue.setName("Venue Name");
		venue.setCapacity(100);
		venue.setRoadName("Oxford Rd, Manchester");
		venue.setPostcode("M13 9PL");
		Double lat = venue.getLatitude();
		Double lon = venue.getLongitude();
		assertNotNull(lat);
		assertNotNull(lon);
		assertEquals(lat, 53.464081, 0.01);
		assertEquals(lon, -2.23148, 0.01);
	}
	
	@Test
	public void invalidAddress() {
		Pair<Double, Double> temp = Geocode.getLatAndLonForAddress("jhgjgjg");
		try {
			Thread.sleep(1000L);
			assertNull(temp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void validAddress() {
		Pair<Double, Double> temp = Geocode.getLatAndLonForAddress("Oxford Rd, Manchester M13 9PL");
		try {
			Thread.sleep(1000L);
			assertNotNull(temp);
			assertEquals(temp.getFirst(), 53.464081, 0.01);
			assertEquals(temp.getSecond(), -2.23148, 0.01);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	


}
