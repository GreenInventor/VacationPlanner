package dmacc.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
