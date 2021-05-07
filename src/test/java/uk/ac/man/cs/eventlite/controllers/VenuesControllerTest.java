package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.ac.man.cs.eventlite.config.Security;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@WebMvcTest(VenuesController.class)
public class VenuesControllerTest {

    @Mock
    private Venue venue;

    @Mock
    private Event event;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VenueService venueService;

    @MockBean
    private EventService eventService;
    
    @Test
    public void deleteVenueWithEvents() throws Exception {
        long id = 1;
        when(event.getVenue()).thenReturn(venue);
        when(venue.getId()).thenReturn((long) 1);
        ArrayList < Event > myEvents = new ArrayList < Event > ();
        myEvents.add(event);
        when(eventService.findAll()).thenReturn(myEvents);
        mvc.perform(delete("/venues/1").with(user("Rob").roles(Security.ADMIN_ROLE)).accept(MediaType.TEXT_HTML).with(csrf())).andExpect(handler().methodName("deleteVenue")).andExpect(status().is(302));
        verify(venueService, never()).deleteById(id);
    }

    @Test
    public void deleteVenues() throws Exception {
        long id = 1;
        when(eventService.findAll()).thenReturn(Collections. < Event > emptyList());
        mvc.perform(delete("/venues/1").with(user("Rob").roles(Security.ADMIN_ROLE)).accept(MediaType.TEXT_HTML).with(csrf())).andExpect(handler().methodName("deleteVenue")).andExpect(status().is(302));
        verify(venueService).deleteById(id);
    }
    
    @Test
    public void NewVenue() throws Exception {
    	mvc.perform(get("/venues/new").with(user("Rob").roles(Security.ADMIN_ROLE))
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
    			.accept(MediaType.TEXT_HTML)
    			.with(csrf()))
    			.andExpect(status().isOk())
    			.andExpect(handler().methodName("newVenue"))
    			.andExpect(view().name("venues/new"));
    }
    
	@Test
	public void goToUpdate() throws Exception {
		when(venueService.findOne(1)).thenReturn(venue);
		
		mvc.perform(get("/venues/update_venue/1").with(user("Rob").roles(Security.ADMIN_ROLE))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.TEXT_HTML)
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(handler().methodName("goToUpdate"))
				.andExpect(view().name("venues/update_venue"));
	}
    
	@Test
	public void SearchVenuesWithEmptyString() throws Exception {
		when(venueService.findAllByName("")).thenReturn(Collections.<Venue>singletonList(venue));
		
		mvc.perform(get("/venues/?keyword=").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(view().name("venues/index")).andExpect(handler().methodName("findVenuesByName"));
		
		verify(venueService).findAllByName("");
	}
	
	@Test
	public void searchVenues() throws Exception {
		when(venueService.findAllByName("venue")).thenReturn(Collections.<Venue>singletonList(venue));
		
		mvc.perform(get("/venues/?keyword=venue").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(view().name("venues/index")).andExpect(handler().methodName("findVenuesByName"));
		
		verify(venueService).findAllByName("venue");
	}
	
	@Test
	public void getVenue() throws Exception {
		when(venueService.findOne(1)).thenReturn(venue);
		
		mvc.perform(get("/venues/1").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("venues/details")).andExpect(handler().methodName("getVenueDetails"));

		verify(venue).getName();
		verify(venue).getRoadName();
		verify(venue).getPostcode();
		verify(venue).getCapacity();
		verify(eventService).findUpcomingEvents();
		verify(eventService).findPastEvents();
	}

    
	@Test
	public void AfterVenueWithNoAuthourization() throws Exception {
		mvc.perform(post("/venues").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "Venue Name")
				.param("roadName", "Road Name")
				.param("postcode", "M13 9PR")
				.param("capacity", "100")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", endsWith("/sign-in")));
		verify(venueService, never()).save(venue);
	}
	
	@Test
	public void AfterVenueIfIncorrectRole() throws Exception {
		mvc.perform(post("/venues").with(user("Rob").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "Venue Name")
				.param("roadName", "Road Name")
				.param("postcode", "M13 9PR")
				.param("capacity", "100")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isForbidden());
		verify(venueService, never()).save(venue);
	}
	
	@Test
	public void AfterVenuetWithNoAuthourization() throws Exception {
		mvc.perform(post("/venues").with(user("Rob").roles(Security.ADMIN_ROLE))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "Venue Name")
				.param("roadName", "Road Name")
				.param("postcode", "M13 9PR")
				.param("capacity", "100")
				.accept(MediaType.TEXT_HTML)).andExpect(status().isForbidden());
		verify(venueService, never()).save(venue);
	}
	
	
	public void AfterVenue() throws Exception {
		ArgumentCaptor<Venue> arg = ArgumentCaptor.forClass(Venue.class);
		mvc.perform(MockMvcRequestBuilders.post("/venues/newVenue").with(user("Organiser").roles(Security.ADMIN_ROLE))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "Venue Name")
				.param("roadName", "Road Name")
				.param("postcode", "M13 9PR")
				.param("capacity", "100")
				.accept(MediaType.TEXT_HTML).with(csrf()))
		.andExpect(status().isFound()).andExpect(content().string(""))
		.andExpect(view().name("redirect:/venues")).andExpect(model().hasNoErrors())
		.andExpect(handler().methodName("newVenue")).andExpect(flash().attributeExists("ok_message"));

		verify(venueService).save(arg.capture());
		assertThat(arg.getValue().getName(), equalTo("Venue Name"));
		assertThat(arg.getValue().getRoadName(), equalTo("Road Name"));
		assertThat(arg.getValue().getPostcode(), equalTo("M13 9PR"));
		assertThat(Integer.toString(arg.getValue().getCapacity()), equalTo("100"));
	}

	@Test
	public void AfterVenueNoData() throws Exception {
		ArgumentCaptor<Venue> arg = ArgumentCaptor.forClass(Venue.class);
		mvc.perform(MockMvcRequestBuilders.post("/venues/newVenue").with(user("Organiser").roles(Security.ADMIN_ROLE))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "")
				.param("roadName", "")
				.param("postcode", "")
				.param("capacity", "")
				.accept(MediaType.TEXT_HTML)).andExpect(status().isForbidden());
		
		verify(venueService, never()).save(venue);
	}
		@Test
	public void updateVenueNoAuthentication() throws Exception {   
		mvc.perform(MockMvcRequestBuilders.post("/venues").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "1")
				.param("name", "testing")
				.param("address", "address")
				.param("postcode", "M15 7GT")
				.param("capacity", "100")
				.accept(MediaType.TEXT_HTML).with(csrf())).andExpect(status().isFound())
				.andExpect(header().string("Location", endsWith("/sign-in")));
		verify(venueService, never()).save(venue);
				
	}
	
	@Test
	@WithMockUser(roles= "ADMINISTRATOR")
	public void updateVenueIncorrectly() throws Exception{ 

	        when(venueService.findOne(0)).thenReturn(null);

	        mvc.perform(MockMvcRequestBuilders.patch("/venues/0").accept(MediaType.TEXT_HTML).with(csrf())
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	                .param("capacity","1000")
	                .param("coordinates","6X 8TY")
	                .param("name","venue")
	                .sessionAttr("venue",venue)
	                .param("description","testing"))
	        .andExpect(status().isMethodNotAllowed());

	}
	
	@Test
	@WithMockUser(roles= "ADMINISTRATOR")
	public void updateVenueWithNoName() throws Exception{

	        when(venueService.findOne(0)).thenReturn(null);

	        mvc.perform(MockMvcRequestBuilders.patch("/venues/0").accept(MediaType.TEXT_HTML).with(csrf())
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	                .param("id","01")
	                .param("address","address")
	                .param("postcode","M15 7GT")
	                .param("capacity","1000")
	                .sessionAttr("venue",venue)
	                .param("description","testing"))
	        .andExpect(status().isMethodNotAllowed());

	}
	
    
}
