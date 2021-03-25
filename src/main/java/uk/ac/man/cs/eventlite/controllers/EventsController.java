package uk.ac.man.cs.eventlite.controllers;

import org.slf4j.LoggerFactory;

import javax.validation.Valid;

import org.slf4j.Logger;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import hello.entities.Greeting;
//import ch.qos.logback.classic.Logger;
import uk.ac.man.cs.eventlite.config.data.InitialDataLoader;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;

@Controller
@RequestMapping(value = "/events", produces = { MediaType.TEXT_HTML_VALUE })
public class EventsController {
	
	private final static Logger log = LoggerFactory.getLogger(InitialDataLoader.class);
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;
	
	@GetMapping
	public String getAllEvents(Model model) {
		model.addAttribute("events", eventService.findAll());
		return "events/index";
	}
	
	@RequestMapping("/delete")
	public String deleteEvent(@RequestParam(value = "id", required = true) Model model, long id)
	{
		eventService.deleteById(id);
		return "redirect:/events";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String goToUpdate(@PathVariable("id") long id, Model model) {
		Event event = eventService.findOne(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		model.addAttribute("name", event.getName());
		model.addAttribute("time", event.getTime());
		model.addAttribute("date", event.getDate());
		model.addAttribute("this_venue", event.getVenue());
		model.addAttribute("all_venues", venueService.findAll());
		
		
		return "/update";
	}
   
	

}

