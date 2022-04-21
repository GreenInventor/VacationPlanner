package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Dealership;

@Repository
public interface DealershipRepository extends JpaRepository<Dealership, Long> {
	public List<Dealership> findByAddressStateOrderByAddressCity(String state);
	}
