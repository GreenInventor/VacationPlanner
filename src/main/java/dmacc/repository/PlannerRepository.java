package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.CarRental;
import dmacc.beans.HotelRental;
import dmacc.beans.Planner;

@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long> {
	public List<Planner> findByAccountId(long id);
	public Planner findAllByCarRentalsIn(List<CarRental> rentals);
	public Planner findAllByHotelRentalsIn(List<HotelRental> hr);
}
