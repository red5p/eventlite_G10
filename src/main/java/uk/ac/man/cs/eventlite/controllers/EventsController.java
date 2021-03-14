package uk.ac.man.cs.eventlite.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Controller
@RequestMapping(value = "/events", produces = { MediaType.TEXT_HTML_VALUE })
public class EventsController {
	
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

	@RequestMapping(method = RequestMethod.GET, value = "/add", produces = { MediaType.TEXT_HTML_VALUE })
	public String addPage(Model model) 
	{
		model.addAttribute("venues", venueService.findAll());

		return "events/add_form";
	} // addPage
	
//    @GetMapping("/add")
//    public String showForm(Model model) {
//        Event event = new Event();
//        model.addAttribute("event", event);
//         
//        Iterable<Venue> venues = venueService.findAll();
//		model.addAttribute("venues", venues);
//         
//        return "add_form";
//    }

	

//	@RequestMapping(method = RequestMethod.GET, value = "/add", produces = { MediaType.TEXT_HTML_VALUE })
//	public String addPage(Model model) 
//	{
//		Event event = new Event();
//		model.addAttribute("event", event);
//		
//		Iterable<Venue> venues = venueService.findAll();
//		model.addAttribute("venues", venues);
//		
//		return "events/add_form";
//	}

/*
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = { MediaType.TEXT_HTML_VALUE })
	public String addEvent(@RequestParam("name") String name,
	   	                   //@RequestParam("date") LocalDate date,
		                   //@RequestParam("time") LocalTime time,
		                   //@RequestParam("venue") Venue venue,
	                       @RequestParam("description") String description,
		                   Model model) throws Exception
	{
		
		Event event = new Event();
		event.setName(name);
		//event.setDate(date);
		//event.setTime(time);
		//event.setVenue(venue);
		event.setDescription(description);	
		
		eventService.save(event);

		return "redirect:/events";
	}
*/
	
	/*@RequestMapping(method = RequestMethod.GET, value = "/add", produces = { MediaType.TEXT_HTML_VALUE })
	public String addPage(Model model) 
	{

		return "events/add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = { MediaType.TEXT_HTML_VALUE })
	public String addEvent(@RequestParam("name") String name,
	                       @RequestParam("description") String description,
	   	                   @RequestParam("date") String date,
		                   @RequestParam("time") String time,
		                   @RequestParam("venue") String venueId,
		                   Model model) throws Exception
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		
		Event event = new Event();
		event.setName(name);
		event.setDescription(description);
		
		try {
			event.setVenue(event.findVenue(Long.parseLong(venueId)));
		} catch (Exception e) {
			event.setVenue(null);
		}
		
		try {
			event.setDate(LocalDate.parse(date));
		} catch (Exception e) {
			event.setDate(null);
		}
		
		try {
			event.setTime(LocalTime.parse(time));
		} catch (Exception e) {
			event.setTime(null);
		}
		
		eventService.save(event);

		return "redirect:/events/" + event.getId();
	}
	
	@GetMapping("/events")
	public String newEventForm(Model model) {
		model.addAttribute("add", new Event());
		return "events/add";
	}

	@PostMapping("/greeting")
	public String eventSubmit(@ModelAttribute Event event) {
	  return "result";
	}

	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createGreeting(@RequestBody @Valid @ModelAttribute Event event, BindingResult errors,
			Model model, RedirectAttributes redirectAttrs) {

		if (errors.hasErrors()) {
			model.addAttribute("event", event);
			return "events/new";
		}

		eventService.save(event);
		redirectAttrs.addFlashAttribute("ok_message", "New event added.");

		return "redirect:/events";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = { MediaType.TEXT_HTML_VALUE })
	public String addEvent(@RequestParam("name") String name,
	                       @RequestParam("description") String description,
	   	                   @RequestParam("date") LocalDate date,
		                   @RequestParam("time") LocalTime time,
		                   @RequestParam("venue") String venueId,
						   Model model) 

	throws Exception{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		
		Event event = new Event();
		event.setName(name);
		event.setDescription(description);
		event.setDate(date);
		event.setTime(time);

		eventService.save(event);

		return "redirect:/events";
	}*/


}

