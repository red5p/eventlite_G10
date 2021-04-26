package uk.ac.man.cs.eventlite.controllers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import uk.ac.man.cs.eventlite.config.Security;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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




}
