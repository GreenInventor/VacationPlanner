package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Event;
import dmacc.beans.EventTicket;

@Repository
public interface EventTicketRepository extends JpaRepository<EventTicket, Long> {
	List<EventTicket> findByEvent(Event event);
}
