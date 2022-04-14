package dmacc.beans;

import java.time.LocalDate;
import java.util.List;

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
public class Car {
	@Id
	@GeneratedValue
	private long id;
	private String make;
	private String model;
	private int year;
	private double pricePerDay;
	private String color;
	private int mpg;
	private int capacity;
	private String fuelType;
	private List<LocalDate> daysRented;
}
