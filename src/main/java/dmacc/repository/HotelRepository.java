package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>{
	public List<Hotel> findByHotelAddressState(String state);
}
