package uk.ac.man.cs.eventlite.controllers;


import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import uk.ac.man.cs.eventlite.config.Security;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;


@ExtendWith(SpringExtension.class)
@WebMvcTest(VenuesControllerApi.class)
@Import(Security.class)
public class VenuesControllerApiTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private VenueService venueService;
	
	@MockBean
	private EventService eventService;

	@Test
	public void putVenueBadAuth() throws Exception {
		mvc.perform(post("/api/venues/update_venue/1")
				.contentType(MediaType.APPLICATION_JSON)
			.content("{ \"name\": \"Venue\", \"capacity\": \"100\", \"street\": \"Street\", \"postCode\": \"M145RL\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void putVenueNotFound() throws Exception {
		when(venueService.existsById(1)).thenReturn(false);
		
		mvc.perform(post("/api/venues/update_venue/1")
				.with(user("Rob").roles(Security.ADMIN_ROLE))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \"Venue\", \"capacity\": \"100\", \"street\": \"Street\", \"postCode\": \"M145RL\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

}
