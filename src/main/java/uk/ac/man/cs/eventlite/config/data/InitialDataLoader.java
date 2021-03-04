package uk.ac.man.cs.eventlite.config.data;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.entities.Event;


@Component
@Profile({ "default", "test" })
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger log = LoggerFactory.getLogger(InitialDataLoader.class);

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (eventService.count() > 0 && venueService.count() > 0) {
			log.info("Database already populated. Skipping data initialization.");
			return;
		}

		// Build and save initial models here.
		
		Venue venue1 = new Venue();
		Venue venue2 = new Venue();
		venue1.setCapacity(80);
		venue1.setId(1);
		venue1.setName("Kilburn, G23");
		venue2.setCapacity(10000);
		venue2.setId(2);
		venue2.setName("online");
		venueService.save(venue1);
		venueService.save(venue2);

		Event event1 = new Event();
		event1.setId(1);
		event1.setDate(LocalDate.parse("2021-05-13"));
		event1.setTime(LocalTime.parse("16:00:00"));
		event1.setName("COMP23412 Showcase, group G");
		event1.setVenue(2);
		eventService.save(event1);
		
		Event event2 = new Event();
		event2.setId(2);
		event2.setDate(LocalDate.parse("2021-05-11"));
		event2.setTime(LocalTime.parse("11:00:00"));
		event2.setName("COMP23412 Showcase, group H");
		event2.setVenue(2);
		eventService.save(event2);
   
	    Event event3 = new Event();
		event3.setId(3);
		event3.setDate(LocalDate.parse("2021-05-10"));
		event3.setTime(LocalTime.parse("16:00:00"));
		event3.setName("COMP23412 Showcase, group F");
		event3.setVenue(2);
		eventService.save(event3);
			
	}
}
