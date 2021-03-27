package uk.ac.man.cs.eventlite.dao;


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

	@Override
	public Iterable<Venue> findAll() {
		return venueRepository.findAll();
	}

	@Override
	public void save(Venue venue) {
		venueRepository.save(venue);
	}



	@Override
	public Venue getById(Long id) {
		return venueRepository.findById(id).orElse(null);

	}

	@Override
	public void deleteById(Long id) {
		venueRepository.deleteById(id);			
	}


	
}
