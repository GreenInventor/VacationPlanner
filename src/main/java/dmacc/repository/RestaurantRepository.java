package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dmacc.beans.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

	List<Restaurant> findByAddressStateOrderByAddressCity(String state);

	List<Restaurant> findByAddressCityOrderByName(String city);

}
