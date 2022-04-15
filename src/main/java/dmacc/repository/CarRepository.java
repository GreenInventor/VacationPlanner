package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Car;
import dmacc.beans.Dealership;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
	public List<Car> findByDealership(Dealership dealership);
}
