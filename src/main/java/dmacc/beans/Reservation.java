package dmacc.beans;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
	@Id
	@GeneratedValue
	private long id;
	@ManyToOne
	private Restaurant restaurant;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private String startTime;
	private String endTime;
	private int numberOfPeople;
}
