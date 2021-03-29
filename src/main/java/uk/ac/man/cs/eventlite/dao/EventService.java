package uk.ac.man.cs.eventlite.dao;

import java.util.List;

import uk.ac.man.cs.eventlite.entities.Event;

public interface EventService {

	public long count();

	public Iterable<Event> findAll();
	
	public void save(Event event);

	public void deleteById(Long id);
	public Event getById(Long id);

	public Iterable<Event> findByKeyword(String k);

	public List<Event> findUpcomingEvents();

	public List<Event> findPastEvents();

	public Iterable<Event> findAllByOrderByDateAscTimeAsc();


}


