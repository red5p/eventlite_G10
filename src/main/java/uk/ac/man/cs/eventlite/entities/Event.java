package uk.ac.man.cs.eventlite.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    private String name;
    
    private String description;

    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;


    public Event() {}


    public Event(Venue venue, String name, LocalDate localdate, LocalTime localtime, String description) {
        this.venue = venue;
        this.name = name;
        this.date = localdate;
        this.time = localtime;
        this.description = description;

    }

    public void setId(long id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate localdate) {
        this.date = localdate;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime localtime) {
        this.time = localtime;
    }


    public Venue getVenue() {
        return venue;
    }


    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public boolean isPast() {
    	if(date == null) {
    		return false;
    	}
    	LocalDate someDate = LocalDate.now();
    	return date.isBefore(someDate);
    }
    
    public boolean isFuture() {
    	if(date == null) {
    		return false;
    	}
    	LocalDate someDate = LocalDate.now();
    	return !date.isBefore(someDate);
    }



}