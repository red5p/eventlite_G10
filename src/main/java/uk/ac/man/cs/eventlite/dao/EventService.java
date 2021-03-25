package uk.ac.man.cs.eventlite.dao;

import ch.qos.logback.classic.Logger;
import uk.ac.man.cs.eventlite.entities.Event;

public interface EventService {

	public long count();

	public Iterable<Event> findAll();
	
	public void save(Event event);

	public void deleteById(Long id);

	public Event findOne(long id);
}

