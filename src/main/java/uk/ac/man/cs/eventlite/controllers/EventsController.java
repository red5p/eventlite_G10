package uk.ac.man.cs.eventlite.controllers;


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
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;

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
   
	

}

