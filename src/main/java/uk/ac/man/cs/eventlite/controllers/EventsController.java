package uk.ac.man.cs.eventlite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Geocoding.twitterImplementation;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

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
import java.util.List;

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
	
	private twitterImplementation twitterService = new twitterImplementation();

	
	
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
			System.out.println(errors + "\n\n\n");
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
		
		List<Double> longitude = new ArrayList<Double>();
		List<Double> latitude = new ArrayList<Double>();
				
		for (Event e:eventService.findUpcomingEvents()) {

			longitude.add(e.getVenue().getLongitude());
			latitude.add(e.getVenue().getLatitude());
		}

		model.addAttribute("longitude", longitude);
		model.addAttribute("latitude", latitude);
		
		List<Status> timeline;
		try {
			timeline = twitterService.getTimeline();
			if (timeline != null) {
				if (timeline.size() > 5) {
					timeline = timeline.subList(0, 5);
				}
				model.addAttribute("timeline", timeline);
			}
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
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
	public String saveUpdate(@PathVariable("id") long eventId, @RequestBody @Valid @ModelAttribute Event event, 
			BindingResult errors,
			Model model
			) {
		if(errors.hasErrors()) {
			return String.format("redirect:/events/%d", eventId);
		}
		eventService.save(event);
		return "redirect:/events";
	}
	
	
	@RequestMapping(value = "/")
	public String findEventsByName(@RequestParam(value="keyword") String keyword, Model model) {
		Iterable<Event> upcomingEvents = eventService.findUpcomingEventsByName(keyword);
		Iterable<Event> pastEvents = eventService.findPastEventsByName(keyword);
		
		List<Double> longitude = new ArrayList<Double>();
		List<Double> latitude = new ArrayList<Double>();
				
		for (Event e:eventService.findUpcomingEvents()) {

			longitude.add(e.getVenue().getLongitude());
			latitude.add(e.getVenue().getLatitude());
		}

		model.addAttribute("longitude", longitude);
		model.addAttribute("latitude", latitude);
		
		List<Status> timeline;
		try {
			timeline = twitterService.getTimeline();
			if (timeline != null) {
				if (timeline.size() > 5) {
					timeline = timeline.subList(0, 5);
				}
				model.addAttribute("timeline", timeline);
			}
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("upcomingevents", upcomingEvents);
		model.addAttribute("pastevents", pastEvents);
		
		return "events/index";
	}
	
	@GetMapping("/{id}")
	public String getEventDetails(@PathVariable("id") long id, Model model) {

		Event event = eventService.findOne(id);
		model.addAttribute("isFuture",event.isFuture());
		model.addAttribute("event", event);
		model.addAttribute("venue", event.getVenue());
		
		return "events/show";
	}

	@PostMapping(value = "/{id}")
	public String tweetEvent(@PathVariable("id") long eventId,
							 @RequestParam("tweet") String tweet, 
							 RedirectAttributes redirectAttrs) throws TwitterException {
    
	Status post = twitterService.postTweet(tweet);
	String message = String.format("Your tweet: %s was posted.", post.getText());
	redirectAttrs.addFlashAttribute("message", message);

	return String.format("redirect:/events/%d", eventId);

    
  }
}
