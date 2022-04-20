package dmacc.beans;

import java.time.LocalDate;

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
public class CarRental {
	@Id
	@GeneratedValue
	private long id;
	@ManyToOne
	private Car car;
	private LocalDate rentalStartDate;
	private LocalDate rentalEndDate;
	private double rentalTotal;
}
