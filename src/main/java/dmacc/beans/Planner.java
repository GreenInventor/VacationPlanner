package dmacc.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carson Allbee callbee
 *CIS175 - Fall 2021
 * Apr 13, 2022
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planner {
	@Id
	@GeneratedValue
	private long id;
	private String destination;
	@ManyToOne
	private Hotel hotel;
	private String activities;
}
