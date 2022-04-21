package dmacc.beans;

import java.time.LocalDate;
import java.util.ArrayList;

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
public class Hotel {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private int roomNumber;
	private ArrayList<LocalDate> daysRented;
	private Address address;
	private double pricePerDay;
}
