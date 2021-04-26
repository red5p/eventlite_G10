package uk.ac.man.cs.eventlite.entities;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.data.util.Pair;

import java.util.Set;
import Geocoding.Geocode;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.Size;


@Entity
@Table(name = "venues")

public class Venue {
	@Id
	@Column(name = "venue_id")
	@GeneratedValue(strategy = GenerationType.AUTO)

	private long id;

	private String name;

	private int capacity;
	
	@OneToMany(targetEntity=Venue.class,mappedBy= "events",cascade=CascadeType.ALL)
	@Column(name = "events")
	private Set<Event> events;
	
	@NotNull(message = "Invalid address")
	private Double latitude;
	
	@NotNull(message = "Invalid address")
	private Double longitude;
	
	@Size(max = 299, message="Road name cannot exceed 299 characters" )
	@NotEmpty(message = "Road name required")
	private String roadName;
	
	@Size(min = 5, max = 7, message="Postcode must be between 6 and 8 characters")
	@NotEmpty(message = "Postcode requried")
	private String postcode;



	
	public Venue() {
	}
	

	
	public Venue(String roadName, String postcode, String name, int capacity) {
		this.capacity = capacity;
		this.name = name;
		this.roadName = roadName;
		this.postcode = postcode;
		
		
		Pair<Double, Double> point = Geocode.getLatAndLonForAddress(this.roadName + " " + this.postcode);
		
		try {
			Thread.sleep(1000L);
			
			if (point != null && point.getFirst() != null && point.getSecond() != null) {
				this.longitude = point.getSecond();
				this.latitude = point.getFirst();

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public String getRoadName() {
		return roadName;
	}


	public void setRoadName(String roadName) {
		this.roadName = roadName;
		
		
		if (this.latitude == null && this.longitude == null && this.roadName != null && this.postcode != null) {
			
			Pair<Double, Double> point = Geocode.getLatAndLonForAddress(this.roadName + " " + this.postcode);
			
			try {
				Thread.sleep(1000L);
				if (point != null && point.getFirst() != null && point.getSecond() != null) {
					this.longitude = point.getSecond();
					this.latitude = point.getFirst();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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

	public String getPostcode() {
		return postcode;
	}
	
	public void setPostcode(String postcode) {
		this.postcode = postcode;
		
		
		if (this.latitude == null && this.longitude == null && this.roadName != null && this.postcode != null) {

			Pair<Double, Double> point = Geocode.getLatAndLonForAddress(this.roadName + " " + this.postcode);
			
			try {
				Thread.sleep(1000L);
				if (point != null && point.getFirst() != null && point.getSecond() != null) {
					this.longitude = point.getSecond();
					this.latitude = point.getFirst();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}


}

