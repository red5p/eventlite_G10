package uk.ac.man.cs.eventlite.dao;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.man.cs.eventlite.entities.Venue;

@Service
public class VenueServiceImpl implements VenueService {	
	@Autowired
	private VenueRepository venueRepository;

	@Override
	public long count() {
		return venueRepository.count();
	}

	public Iterable<Venue> findAll() {
		Iterable<Venue> vs = venueRepository.findAll();
		List<Venue> venues = new ArrayList<Venue>();
		
		for (Venue v: vs) {
			venues.add(v);

		}
	
		venues.sort((a1,a2) -> {
			return a1.getName().compareTo(a2.getName());
		});
		
		return venues;
	}

	@Override
	public void save(Venue venue) {
		venueRepository.save(venue);
	}

	@Override
	public Venue findOne(long id) {
		return venueRepository.findById(id).orElse(null);
	}

	@Override
	public Iterable<Venue> findAllByName(String name) {
		return venueRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name);
	}
	
	@Override
	public void deleteById(long id)
	{
		venueRepository.deleteById(id);
	}


}
