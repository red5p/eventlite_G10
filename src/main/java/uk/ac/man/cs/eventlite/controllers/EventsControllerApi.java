package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;



import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import uk.ac.man.cs.eventlite.dao.EventService;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.response.EventResponse;
import uk.ac.man.cs.eventlite.response.VenueResponse;

@RestController
@RequestMapping(value = "/api/events", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class EventsControllerApi {

	@Autowired
	private EventService eventService;
	
	@GetMapping
	public CollectionModel<Event> getAllEvents() {

		return eventCollection(eventService.findAll());
	}
	

	private CollectionModel<Event> eventCollection(Iterable<Event> events) {
		Link selfLink = linkTo(methodOn(EventsControllerApi.class).getAllEvents()).withSelfRel();

		return CollectionModel.of(events, selfLink);
	}

//------Single Event------------------------------------------------------------------------------------
	
	@GetMapping("/{id}")
	public EventResponse venue(@PathVariable("id") long id) {
		Event theEvent = eventService.findOne(id);
		
		return detailedEvent(theEvent);
	}
	
	private EventResponse detailedEvent(Event event) {
		
		EventResponse theEvent = new EventResponse();
		theEvent.date = event.getDate();
		theEvent.time = event.getTime();
		theEvent.name = event.getName();
		
		Link selfLink = linkTo(EventsControllerApi.class).slash(event.getId()).withSelfRel();
		Link eventLink = linkTo(EventsControllerApi.class).slash(event.getId()).withRel("event");
		Link venueLink = linkTo(EventsControllerApi.class).slash(event.getId()).slash("venue").withRel("venue");
		
		theEvent.add(selfLink, eventLink, venueLink);
		
		return theEvent;
		
	}
	
	
	@DeleteMapping("/delete/{id}")
	public void deleteEvent(@PathVariable Long id) {
		eventService.deleteById(id);
	}
	

	@GetMapping("/new")
	public ResponseEntity<?> newEvent(){
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createEvent(@RequestBody @Valid Event event, BindingResult result){
		if(result.hasErrors()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		eventService.save(event);
		URI location = linkTo(EventsControllerApi.class).slash(event.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	


}
