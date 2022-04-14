package dmacc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Planner;

/**
 * @author Carson Allbee callbee
 *CIS175 - Fall 2021
 * Apr 13, 2022
 */
@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long>{

}
