package uk.ac.man.cs.eventlite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;

@Controller
@RequestMapping(value = "/venues", produces = {
    MediaType.TEXT_HTML_VALUE
})
public class VenueController {

    @Autowired
    private VenueService venueService;

    @Autowired
    private EventService eventService;

	@DeleteMapping("/{id}")
	public String deleteVenue(@PathVariable("id") long myId, RedirectAttributes RedirectAttributes) {
		Iterable<uk.ac.man.cs.eventlite.entities.Event> myEvents = eventService.findAll();
		for (uk.ac.man.cs.eventlite.entities.Event event : myEvents) {
			if (event.getVenue().getId()==myId) {
				RedirectAttributes.addFlashAttribute("error_message", "Events associated with venue so cannot be removed.");				
				return "redirect:/venues";
			}
		}
		venueService.deleteById(myId);
		RedirectAttributes.addFlashAttribute("ok_message", "Venue removed.");
		return "redirect:/venues";
	}
	
}