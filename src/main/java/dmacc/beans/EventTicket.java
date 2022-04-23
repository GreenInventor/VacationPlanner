package dmacc.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTicket {
	@Id
	@GeneratedValue
	private long id;
	@ManyToOne
	private Event event;
	private int numberOfTickets;
	private double totalPrice;
}
