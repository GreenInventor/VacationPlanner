package dmacc.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carson Allbee callbee
 *CIS175 - Fall 2021
 * Apr 19, 2022
 */
@Entity
@Data
public class Activities {

	@Id
	@GeneratedValue
	private long id;
	private String activity;
	private double price;
	
	public Activities() {
		super();
	}
	
	public Activities(long id, String activity, double price) {
		super();
		this.id = id;
		this.activity = activity;
		this.price = price;
	}
	
	public Activities(String activity, double price) {
		super();
		this.activity = activity;
		this.price = price;
	}
	
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
}
