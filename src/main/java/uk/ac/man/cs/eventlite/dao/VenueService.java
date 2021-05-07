package uk.ac.man.cs.eventlite.dao;

import java.util.Optional;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public interface VenueService {

	public long count();

	public Iterable<Venue> findAll();
	
	void save(Venue venue);

	public Venue findOne(long id);
	
	public Iterable<Venue> findAllByName(String name);

	public void deleteById(long eventid);

	public Object existsById(int i);


}
