package uk.ac.man.cs.eventlite.entities;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@Table(name = "venues")

public class Venue {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private long id;

	private String name;

	private int capacity;

	public Venue() {
	}
	
	public Venue(String name, int capacity) {
		this.capacity = capacity;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
