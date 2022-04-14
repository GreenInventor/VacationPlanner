package dmacc.beans;

/**
 * @author Carson Allbee callbee
 *CIS175 - Fall 2021
 * Apr 13, 2022
 */
public class Planner {

	private String destination;
	private Hotel hotel;
	private String activities;
	
	
	public Planner() {
		super();
	}
	
	public Planner(String destination, Hotel hotel, String activities) {
		super();
		this.destination = destination;
		this.hotel = hotel;
		this.destination = destination;
	}
	
	public Planner(String destination, String activities) {
		super();
		this.destination = destination;
		this.activities = activities;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the hotel
	 */
	public Hotel getHotel() {
		return hotel;
	}

	/**
	 * @param hotel the hotel to set
	 */
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	/**
	 * @return the activities
	 */
	public String getActivities() {
		return activities;
	}

	/**
	 * @param activities the activities to set
	 */
	public void setActivities(String activities) {
		this.activities = activities;
	}
	
	
	
}
