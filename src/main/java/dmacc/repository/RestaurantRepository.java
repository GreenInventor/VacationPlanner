package dmacc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dmacc.beans.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

}
