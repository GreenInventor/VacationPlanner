package dmacc.beans;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private double ticketPrice;
	private int avalibleTickets;
	private Address address;
	private String phoneNumber;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
}
