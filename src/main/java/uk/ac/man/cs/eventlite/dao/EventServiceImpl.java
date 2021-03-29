package uk.ac.man.cs.eventlite.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Override
	public long count() {
		return eventRepository.count();
	}

	@Override
	public Iterable<Event> findAll() {
		return eventRepository.findAll();
	}
	
	@Override
	public Iterable<Event> findAllByOrderByDateAscTimeAsc() {
		return eventRepository.findAllByOrderByDateAscTimeAsc();
	}
	
	@Override
	public void save(Event event) {
		eventRepository.save(event);
	}

	@Override
	public void deleteById(Long id) {
		eventRepository.deleteById(id);		

	}	
	
	
	}
	
	@Override
	public Event getById(Long id) {
		return eventRepository.findById(id).orElse(null);
	}
	
	@Override
	public Iterable<Event> findByKeyword(String k) {
		Iterable<Event> events = eventRepository.findAllByOrderByDateAscTimeAsc();
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event e:events) {
			if (e.getName().toLowerCase().contains(k.toLowerCase())) {
				result.add(e);
			}
		}
		return result;


	}
	
	@Override
	public List<Event> findUpcomingEvents() {
		List<Event> upcomingEvents = new ArrayList<Event>();
		Iterable<Event> events = eventRepository.findAll();
		for (Event e:events) {
			if (e.isFuture()) {
				upcomingEvents.add(e);
			}
		}
		
		upcomingEvents.sort((a1,a2) -> {
			if(a1.getDate().equals(a2.getDate())) {
				return a1.getName().compareTo(a2.getName());
			}else{
				return a1.getDate().compareTo(a2.getDate());
			}
		});

		return upcomingEvents;
	} // findUpcomingEvents


	@Override
	public List<Event> findPastEvents() {
		List<Event> pastEvents = new ArrayList<Event>();
		Iterable<Event> events = eventRepository.findAll();
		for (Event e:events) {
			if (e.isPast()) {
				pastEvents.add(e);
			}
		}
		
		pastEvents.sort((a1,a2) -> {
			if(a1.getDate().equals(a2.getDate())) {
				return a1.getName().compareTo(a2.getName());
			}else{
				return a2.getDate().compareTo(a1.getDate());
			}
		});
		
		return pastEvents;

	}

}
