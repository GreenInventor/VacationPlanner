package dmacc.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import dmacc.beans.Hotel;
import dmacc.beans.HotelRental;

@Repository
public interface HotelRentalRepository extends JpaRepository<HotelRental, Long> {

	List<HotelRental> findByHotel(Hotel hotel);
}
