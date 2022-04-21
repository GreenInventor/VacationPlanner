package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Car;
import dmacc.beans.CarRental;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, Long> {
	List<CarRental> findByCar(Car car);
}
