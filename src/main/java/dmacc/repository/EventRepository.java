package dmacc.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByAddressStateAndAvalibleTicketsGreaterThanAndDateInOrderByAddressCity(String state, int tickets, List<LocalDate> date);

	List<Event> findByAddressCityAndAvalibleTicketsGreaterThanAndDateInOrderByName(String city, int tickets, List<LocalDate> date);

}
