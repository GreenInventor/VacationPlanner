package dmacc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Activities;

@Repository
public interface ActivityRepository extends JpaRepository<Activities, Long> {

}
