package uk.ac.man.cs.eventlite.dao;

import org.springframework.data.repository.CrudRepository;


import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;


public interface EventRepository extends CrudRepository<Event, Long> {
	public Iterable<Event> findByNameContainingIgnoreCase(String name);
}
