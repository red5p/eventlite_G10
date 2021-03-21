package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Venue;

public interface VenueService {

	public long count();

	public Iterable<Venue> findAll();

	void save(Venue venue);

	public void deleteById(long id);

	public Venue getById(Long id);

	void deleteById(Long id);}
