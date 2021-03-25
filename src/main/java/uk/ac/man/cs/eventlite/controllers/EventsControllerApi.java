package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import hello.entities.Greeting;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@RestController
@RequestMapping(value = "/api/events", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class EventsControllerApi {

	@Autowired
	private EventService eventService;
	@Autowired
	private VenueService venueService;

	@GetMapping
	public CollectionModel<Event> getAllEvents() {

		return eventCollection(eventService.findAll());
	}
	

	private EntityModel<Event> singleEvent(Event event) {
		Link selfLink = linkTo(EventsControllerApi.class).slash(event.getId()).withSelfRel();

		return EntityModel.of(event, selfLink);
	}

	private CollectionModel<Event> eventCollection(Iterable<Event> events) {
		Link selfLink = linkTo(methodOn(EventsControllerApi.class).getAllEvents()).withSelfRel();

		return CollectionModel.of(events, selfLink);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteEvent(@PathVariable Long id) {
		eventService.deleteById(id);
	}
	
	@PostMapping(value = "/update/{id}")
	public String saveUpdate(@RequestBody @Valid @ModelAttribute Event event, 
			BindingResult errors,
			Model model,
			@PathVariable("id") long id,
			@PathVariable("name") String new_name,
			@PathVariable("venue") Venue new_venue,
			@PathVariable("time") String new_time,
			@PathVariable("date") String new_date
			) {
		if(errors.hasErrors()) {
			return "redirect:/update/" + id;
		}
		Event oldEvent = eventService.findOne(id);
		event.setVenue(oldEvent.getVenue());
		
		for(Venue v: venueService.findAll()) {
			System.out.println("Venue name: "+ v.getName());
			if((v.getName()).equals(new_venue.getName())) {
				event.setVenue(v);
			}
		}
		event.setName(new_name);
		LocalDate formatteddate = LocalDate.parse(new_date, DateTimeFormatter.ofPattern("yyyy-mm-dd"));
		LocalTime formattedtime = LocalTime.parse(new_time, DateTimeFormatter.ofPattern("HH:mm"));
		event.setDate(formatteddate);
		event.setTime(formattedtime);
		eventService.save(event);
		return "redirect:/events";
	}
	
}
