package uk.ac.man.cs.eventlite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

import javax.validation.Valid;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

import uk.ac.man.cs.eventlite.dao.EventService;

@Controller
@RequestMapping(value = "/events", produces = { MediaType.TEXT_HTML_VALUE })
public class EventsController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;
	
	
	
	@GetMapping("/new")
	public String newEvent(Model model) {
		if(!model.containsAttribute("events")) {
			model.addAttribute("events", new Event());
		}
		
		model.addAttribute("venues", venueService.findAll());
		
		return "events/new";
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createEvent(@RequestBody @Valid @ModelAttribute Event event, BindingResult errors,
			Model model, RedirectAttributes redirectAttrs){
		
		if(errors.hasErrors()) {
			System.out.println(errors);
			model.addAttribute("events", event);
			model.addAttribute("venues", venueService.findAll());
			
			return "events/new";
		}
		
		eventService.save(event);
		redirectAttrs.addFlashAttribute("ok_message", "New event successfully added!");
		
		return "redirect:/events";
		
	}



	@GetMapping
	public String getAllEvents(Model model) {
		model.addAttribute("events", eventService.findAll());
		model.addAttribute("upcomingevents", eventService.findUpcomingEvents());
		model.addAttribute("pastevents", eventService.findPastEvents());
		return "events/index";
	}

	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") long id) {
		eventService.deleteById(id);
		return "redirect:/events"; 
	}	
	
	
	@GetMapping(value = "/update/{id}")
    public String goToUpdate(@PathVariable("id") long id, Model model) {
		Event event = eventService.findOne(id);
		model.addAttribute("venues", venueService.findAll());
		model.addAttribute("event", event);	
		return "events/update";
	}
	@PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String saveUpdate(@RequestBody @Valid @ModelAttribute Event event, 
			BindingResult errors,
			Model model
			) {
		if(errors.hasErrors()) {
			return "redirect:/update/" + event.getId();
		}
		eventService.save(event);
		return "redirect:/events";
	}
	
	@RequestMapping(value = "/")
	public String findEventsByName(@RequestParam(value="keyword") String keyword, Model model) {
		Iterable<Event> upcomingEvents = eventService.findUpcomingEventsByName(keyword);
		Iterable<Event> pastEvents = eventService.findPastEventsByName(keyword);
		
		//model.addAttribute("events", allEvents);
		model.addAttribute("upcomingevents", upcomingEvents);
		model.addAttribute("pastevents", pastEvents);
		
		return "events/index";
	}
}
