package uk.ac.man.cs.eventlite.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Controller
@RequestMapping(value = "/venues", produces = { MediaType.TEXT_HTML_VALUE })
public class VenuesController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;


	@GetMapping
	public String getAllvenues(Model model) {
		model.addAttribute("venues", venueService.findAll());
		return "venues/index";
	}
	
	@GetMapping("/{id}")
	public String getVenueDetails(@PathVariable("id") long id, Model model) {

		Venue venue = venueService.findOne(id);
		model.addAttribute("venue", venue);
		
		List<Event> events = eventService.findUpcomingEvents();
		List<Event> upcomingEvents = new ArrayList<Event>();
		for(Event e:events) {
			if(e.getVenue().getId() == id) {
				upcomingEvents.add(e);
			}
		}
		
		events = eventService.findPastEvents();
		List<Event> pastEvents = new ArrayList<Event>();
		for(Event e:events) {
			if(e.getVenue().getId() == id) {
				pastEvents.add(e);
			}
		}
		
		model.addAttribute("pastevents", pastEvents);
		model.addAttribute("upcomingevents", upcomingEvents);
		return "venues/details";
	}
	
	@RequestMapping(value = "/")
	public String findVenuesByName(@RequestParam(value="keyword") String keyword, Model model) {
		Iterable<Venue> venues = venueService.findAllByName(keyword);

		model.addAttribute("venues", venues);
		
		return "venues/index";
	}

}
