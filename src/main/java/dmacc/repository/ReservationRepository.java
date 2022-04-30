package dmacc.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Reservation;
import dmacc.beans.Restaurant;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> getByRestaurant(Restaurant r);

}
