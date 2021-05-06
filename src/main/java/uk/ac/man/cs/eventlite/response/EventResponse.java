package uk.ac.man.cs.eventlite.response;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.hateoas.RepresentationModel;

public class EventResponse extends RepresentationModel<EventResponse>{

	public LocalDate date;

	public LocalTime time;
	
	public String name;

}