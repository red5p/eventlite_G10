package uk.ac.man.cs.eventlite.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.man.cs.eventlite.entities.Event;

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
//		return eventRepository.findAllByOrderByDateAsc();
		return eventRepository.findAllByOrderByDateAscTimeAsc();
		
//		Iterable<Event> events = eventRepository.findAll();
//		Iterator<Event> I = events.iterator();
//		List<Event> arr = new ArrayList<Event>();
//		while(I.hasNext()) {
//			arr.add(I.next());
//		}
//		
//		arr.sort((a1,a2) -> {
//			if(a1.getDate().equals(a2.getDate())) {
//				return a1.getTime().compareTo(a2.getTime());
//			}else{
//				return a1.getDate().compareTo(a2.getDate());
//			}
//		});
//		
//		return arr;
	}
	
	
	@Override
	public void save(Event event) {
		eventRepository.save(event);
	}


	@Override
	public void deleteById(Long id) {
		eventRepository.deleteById(id);		
	}
	
	@Override
	public Iterable<Event> findByName(String name) {
		Iterable<Event> events = eventRepository.findAll();
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event e:events) {
			if (e.getName().contains(name)) {
				result.add(e);
			}
		}
		return result;

	}
}
