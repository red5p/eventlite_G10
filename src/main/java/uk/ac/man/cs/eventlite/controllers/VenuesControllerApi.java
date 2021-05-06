package uk.ac.man.cs.eventlite.controllers;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.EntityMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.response.VenueResponse;

@RestController
@RequestMapping(value = "/api/venues", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class VenuesControllerApi {
	
	@Autowired
	private VenueService venueService;
	
	@Autowired
	private EventService eventService;
	
	@GetMapping
	public CollectionModel<Venue> getAllVenues() {
		
		return venueCollection(venueService.findAll());
	}
	
	private CollectionModel<Venue> venueCollection(Iterable<Venue> venues) {
		Link selfLink = linkTo(methodOn(VenuesControllerApi.class).getAllVenues()).withSelfRel();
		Link profileLink = linkTo(VenuesControllerApi.class).slash("profile").slash("venues").withRel("profile");

		Link[] links = {selfLink, profileLink};
		
		return CollectionModel.of(venues, links);
	}

//------Single Venue------------------------------------------------------------------------------------
	
	@GetMapping("/{id}")
	public VenueResponse venue(@PathVariable("id") long id) {
		Venue theVenue = venueService.findOne(id);
		
		return detailedVenue(theVenue);
	}
	
	private VenueResponse detailedVenue(Venue venue) {
			
		VenueResponse theVenue = new VenueResponse();
		theVenue.name = venue.getName();
		theVenue.capacity = venue.getCapacity();
		
		Link selfLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).withSelfRel();
		Link venueLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).withRel("venue");
		Link eventsLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).slash("events").withRel("events");
		Link next3eventsLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).slash("next3events").withRel("next3events");
		theVenue.add(selfLink, venueLink, eventsLink, next3eventsLink);
		
		return theVenue;
		
	}

//------Next 3 Events------------------------------------------------------------------------------------
	
	@GetMapping("/{id}/next3events")
	public CollectionModel<Event> nextThreeEvents(@PathVariable("id") long id){
		
		List<Event> events = eventService.findUpcomingEvents();
		List<Event> upcomingEvents = new ArrayList<Event>();

		while(upcomingEvents.size() < 3) {
			for(Event e:events) {
				if(e.getVenue().getId() == id) {
					upcomingEvents.add(e);
				}
			}
		}
		
		return eventCollection(upcomingEvents);
	}

//------Events Link------------------------------------------------------------------------------------

	@GetMapping("/{id}/events")
	public CollectionModel<Event> eventsAtVenue(@PathVariable("id") long id){
		
		List<Event> events = eventService.findUpcomingEvents();
		List<Event> upcomingEvents = new ArrayList<Event>();

		for(Event e:events) {
			if(e.getVenue().getId() == id) {
				upcomingEvents.add(e);
			}
		}

		return eventCollection(upcomingEvents);
	}

	private CollectionModel<Event> eventCollection(Iterable<Event> events) {
		Link selfLink = linkTo(methodOn(EventsControllerApi.class).getAllEvents()).withSelfRel();

		return CollectionModel.of(events, selfLink);
	}
//------------------------------------------------------------------------------------------------------	

	
	@GetMapping("/new")
	public ResponseEntity<?> newVenue(){
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createVenue(@RequestBody @Valid Venue venue, BindingResult result){
		if(result.hasErrors()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		venueService.save(venue);
		URI location = linkTo(EventsControllerApi.class).slash(venue.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
				
	
		
}
