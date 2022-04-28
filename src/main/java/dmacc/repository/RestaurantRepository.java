package dmacc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dmacc.beans.Restaurant;

/**
 * @author Carson Allbee callbee
 *CIS175 - Fall 2021
 * Apr 27, 2022
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

}
