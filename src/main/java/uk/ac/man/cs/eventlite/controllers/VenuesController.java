package uk.ac.man.cs.eventlite.controllers;

import java.io.IOException;

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

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import retrofit2.Response;


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
	public String greeting(@PathVariable("id") long id, Model model) {

		Venue venue = venueService.findOne(id);
		model.addAttribute("venue", venue);
		
		return "venues/details";
	}
	
	@RequestMapping(value = "/")
	public String findVenuesByName(@RequestParam(value="keyword") String keyword, Model model) {
		Iterable<Venue> venues = venueService.findAllByName(keyword);

		model.addAttribute("venues", venues);
		
		return "venues/index";
	}

	
	
	@GetMapping("/new")
	public String newVenue(Model model) {
		if(!model.containsAttribute("venues")) {
			model.addAttribute("venues", new Venue());
		}
		System.out.print("PRINT THIS");
		return "venues/new";
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createVenue(@RequestBody @Valid @ModelAttribute Venue venue, BindingResult errors,
			Model model, RedirectAttributes redirectAttrs){

		if(errors.hasErrors()) {
			System.out.print(errors);
			model.addAttribute("venues", venue);
			
			return "venues/new";
		}

		
		venueService.save(venue);
		redirectAttrs.addFlashAttribute("ok_message", "New venue successfully added!");
		
		return "redirect:/venues";
		
	}
	
}
