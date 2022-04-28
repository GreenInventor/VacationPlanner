package dmacc.beans;

/**
 * @author Carson Allbee callbee
 *CIS175 - Fall 2021
 * Apr 27, 2022
 */
public class Restaurant {

	private long id;
	private String name;
	private String address;
	
	public Restaurant() {
		super();
	}
	
	public Restaurant(long id, String name, String address) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
	}
	
	public Restaurant(String name, String address) {
		super();
		this.name = name;
		this.address = address;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
