package uk.ac.man.cs.eventlite.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import uk.ac.man.cs.eventlite.config.Security;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import java.time.LocalDate;
import java.time.LocalTime;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;


@WebMvcTest(EventsController.class)
@Import(Security.class)
public class EventsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private Event event;

    @Mock
    private Venue venue;

    @MockBean
    private EventService eventService;

    @MockBean
    private VenueService venueService;


    private final static String BAD_ROLE = "USER";
    String tempTimeTest = LocalTime.parse("18:00").toString();

    String todaysDate = LocalDate.now().toString();
    String tomorrowsDate = LocalDate.now().plusDays(1).toString();
    String previoiusDate = LocalDate.parse("2021-01-01").toString();

    @Test
    public void getIndexWhenNoEvents() throws Exception {
        when(eventService.findAll()).thenReturn(Collections. < Event > emptyList());
        when(venueService.findAll()).thenReturn(Collections. < Venue > emptyList());

        mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
            .andExpect(view().name("events/index")).andExpect(handler().methodName("getAllEvents"));

        verify(eventService).findAll();
        verifyNoInteractions(event);
        verifyNoInteractions(venue);
    }

    @Test
    public void getIndexWithEvents() throws Exception {
        when(venue.getName()).thenReturn("Kilburn Building");
        when(venueService.findAll()).thenReturn(Collections. < Venue > singletonList(venue));

        when(event.getVenue()).thenReturn(venue);
        when(eventService.findAll()).thenReturn(Collections. < Event > singletonList(event));

        mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
            .andExpect(view().name("events/index")).andExpect(handler().methodName("getAllEvents"));

        verify(eventService).findAll();
    }

    @Test
    public void deleteAnEvent() throws Exception {
        long id = 1;

        when(eventService.findOne(id)).thenReturn(event);
        mvc.perform(delete("/events/1").with(user("Rob").roles(Security.ADMIN_ROLE)).accept(MediaType.TEXT_HTML).with(csrf())).andExpect(view().name("redirect:/events")).andExpect(status().isFound());

    }
    
    
	@Test
	public void addEvent() throws Exception{
		ArgumentCaptor<Event> arg = ArgumentCaptor.forClass(Event.class);
	
		mvc.perform(post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("name", "test").param("date", LocalDate.now().plusDays(1).toString()).param("venue.id", "01")
			.accept(MediaType.TEXT_HTML)
			.with(csrf()))
			.andExpect(model().hasNoErrors())
			.andExpect(status().isFound())
			.andExpect(handler().methodName("createEvent"))
			.andExpect(view().name("redirect:/events"));
		
		verify(eventService).save(arg.capture());
	}
	
	

	@Test
	public void getEvent() throws Exception{
		when(eventService.findOne(1)).thenReturn(event);
		when(event.getVenue()).thenReturn(venue);
		
		
		mvc.perform(get("/events/1").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("events/show")).andExpect(handler().methodName("greeting"));
		
		verify(eventService).findOne(1);
	}
	

	public void getAllEventsByName() throws Exception {
		
		when(eventService.findAll()).thenReturn(Collections.<Event>singletonList(event));
		
		mvc.perform(get("/events?keyword=abc").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(handler().methodName("getAllEvents")).andExpect(view().name("events/index"));
		
		verify(eventService).findUpcomingEventsByName("abc");
	}


    @Test
    public void afterEvent() throws Exception {
        ArgumentCaptor < Event > argument = ArgumentCaptor.forClass(Event.class);

        mvc.perform(post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Event Name")
                .param("venue.id", "2")
                .param("date", tomorrowsDate)
                .param("time", tempTimeTest)
                .param("description", "Very good event")
                .accept(MediaType.TEXT_HTML).with(csrf()))
            .andExpect(status().isFound()).andExpect(model().hasNoErrors())
            .andExpect(view().name("redirect:/events"))
            .andExpect(handler().methodName("createEvent"))
            .andExpect(flash().attributeExists("ok_message"));

        verify(eventService).save(argument.capture());
        assertThat("Event Name", equalTo(argument.getValue().getName()));
        assertThat(2L, equalTo(argument.getValue().getVenue().getId()));
        assertThat(tomorrowsDate, equalTo(argument.getValue().getDate().toString()));
        assertThat(tempTimeTest, equalTo(argument.getValue().getTime().toString()));
        assertThat("Very good event", equalTo(argument.getValue().getDescription()));
    }

    @Test
    public void AfterEventWithNoAuthourization() throws Exception {
        mvc.perform(post("/events").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Event Name")
                .param("venue", "2")
                .param("date", tomorrowsDate)
                .param("time", tempTimeTest)
                .param("description", "Very good event")
                .accept(MediaType.TEXT_HTML).with(csrf()))
            .andExpect(status().isFound())
            .andExpect(header().string("Location", endsWith("/sign-in")));
        verify(eventService, never()).save(event);
    }


    public void AfterEventIfIncorrectRole() throws Exception {
        mvc.perform(
                post("/events").with(user("Rob").roles(BAD_ROLE)).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Event Name")
                .param("venue", "2")
                .param("date", tomorrowsDate)
                .param("time", tempTimeTest)
                .param("description", "Very good event")
                .accept(MediaType.TEXT_HTML).with(csrf()))
            .andExpect(status().isForbidden());
        verify(eventService, never()).save(event);
    }

    @Test
    public void afterEventWithPreviousDate() throws Exception {
        mvc.perform(post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Event Name")
                .param("venue.id", "2")
                .param("date", previoiusDate)
                .accept(MediaType.TEXT_HTML)
                .with(csrf())).andExpect(status().isOk())
            .andExpect(view().name("events/new"))
            .andExpect(model().attributeHasFieldErrors("event", "date"))
            .andExpect(handler().methodName("createEvent"))
            .andExpect(flash().attributeCount(0));
        verify(eventService, never()).save(event);
    }

    @Test
    public void AfterEventIfNotRole() throws Exception {
        mvc.perform(post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "Event Name")
            .param("venue", "2")
            .param("date", tomorrowsDate)
            .param("time", tempTimeTest)
            .param("description", "Very good event")
            .accept(MediaType.TEXT_HTML)).andExpect(status().isForbidden());
        verify(eventService, never()).save(event);
    }

    @Test
    public void AfterEventWithPresentDate() throws Exception {
        mvc.perform(post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Event Name")
                .param("venue.id", "2")
                .param("date", todaysDate)
                .accept(MediaType.TEXT_HTML)
                .with(csrf())).andExpect(status().isOk())
            .andExpect(view().name("events/new"))
            .andExpect(model().attributeHasFieldErrors("event", "date"))
            .andExpect(handler().methodName("createEvent"))
            .andExpect(flash().attributeCount(0));
        verify(eventService, never()).save(event);
    }

    @Test
    public void incorrectTimeForEvent() throws Exception {
        mvc.perform(post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Event Name")
                .param("venue", "2")
                .param("date", tomorrowsDate)
                .param("time", "12:o1")
                .param("description", "Very good event")
                .accept(MediaType.TEXT_HTML).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("events/new"))
            .andExpect(model().attributeHasFieldErrors("event", "time"))
            .andExpect(handler().methodName("createEvent"))
            .andExpect(flash().attributeCount(0));
        verify(eventService, never()).save(event);
    }

	@Test
	public void afterEvenExcludingOptopnalFields() throws Exception {
	    ArgumentCaptor < Event > arg = ArgumentCaptor.forClass(Event.class);

	    mvc.perform(post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .param("name", "Event Name")
	            .param("venue.id", "2")
	            .param("date", tomorrowsDate)
	            .accept(MediaType.TEXT_HTML).with(csrf()))
	        .andExpect(status().isFound()).andExpect(model().hasNoErrors())
	        .andExpect(view().name("redirect:/events"))
	        .andExpect(handler().methodName("createEvent"))
	        .andExpect(flash().attributeExists("ok_message"));

	    verify(eventService).save(arg.capture());
	    assertThat("Event Name", equalTo(arg.getValue().getName()));
	    assertThat(2L, equalTo(arg.getValue().getVenue().getId()));
	    assertThat(tomorrowsDate, equalTo(arg.getValue().getDate().toString()));
	}

    @Test
    public void afterEventWithMissingVenue() throws Exception {
        mvc.perform(post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Event Name")
                .param("venue.id", "")
                .param("date", tomorrowsDate)
                .accept(MediaType.TEXT_HTML)
                .with(csrf())).andExpect(status().isOk())
            .andExpect(view().name("events/new"))
            .andExpect(model().attributeHasFieldErrors("event", "venue.id"))
            .andExpect(handler().methodName("createEvent"))
            .andExpect(flash().attributeCount(0));
        verify(eventService, never()).save(event);
    }
    @Test
    @WithMockUser(roles= "ADMINISTRATOR")
    public void updateEventInvalid() throws Exception{

            when(eventService.findById(0)).thenReturn(null);

            mvc.perform(MockMvcRequestBuilders.patch("/events/0").accept(MediaType.TEXT_HTML).with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .param("date","2020-12-12")
                    .param("time","09:00")
                    .param("name","test event")
                    .sessionAttr("venue",venue)
                    .param("description","testing"))
            .andExpect(status().isMethodNotAllowed());

    }

    @Test
    public void updateEventNoAuthentication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/venues").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test event")
                .param("id", "99")
                .param("date", "2020-12-12")
                .param("time", "09:00")
                .param("description","testing")
                .accept(MediaType.TEXT_HTML).with(csrf())).andExpect(status().isFound())
                .andExpect(header().string("Location", endsWith("/sign-in")));
        verify(eventService, never()).save(event);

    }

    @Test
    public void updateEventNoCsrf() throws Exception{

            mvc.perform(MockMvcRequestBuilders.post("/events").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .with(user("Rob").roles(Security.ADMIN_ROLE))
                    .param("name","test event")
                    .param("id","10")
                    .param("date", "2020-12-12")
                    .param("time", "09:00")
                    .param("description","testing"))
            .andExpect(status().isForbidden());

    }
}
