package dmacc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Planner;

@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long>{

}
