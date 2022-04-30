package dmacc.beans;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planner {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String destination;
	@OneToMany
	private List<HotelRental> hotelRentals;
	@ManyToMany
	private List<Activity> activities;
	@ManyToMany
	private List<EventTicket> events;
	@ManyToMany
	private List<Reservation> reservations;
	@ManyToOne
	private Account account;
	@OneToMany
	private List<CarRental> carRentals;
}
