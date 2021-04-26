package uk.ac.man.cs.eventlite.dao;


import java.util.List;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public interface EventService {

	public long count();

	public Iterable<Event> findAll();
	
	public void save(Event event);

	public void deleteById(Long id);

	public Event findOne(long id);

	List<Event> findUpcomingEvents();

	List<Event> findPastEvents();
	
	public Iterable<Event> findPastEventsByName(String keyword);
	
	public Iterable<Event> findUpcomingEventsByName(String keyword);


}

