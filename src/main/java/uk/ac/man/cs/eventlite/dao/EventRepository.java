package uk.ac.man.cs.eventlite.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import uk.ac.man.cs.eventlite.entities.Event;


public interface EventRepository extends CrudRepository<Event, Long> {
	public Iterable<Event> findAllByOrderByDateAscTimeAsc();
	public Iterable<Event> findAllByOrderByDateAsc();
	public Iterable<Event> findAllByOrderByDateAscNameAsc();
}
