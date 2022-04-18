package dmacc.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.CarRental;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, Long> {
}
