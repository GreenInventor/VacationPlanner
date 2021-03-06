package dmacc.beans;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private String startTime;
	private String endTime;
}
