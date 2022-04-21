package dmacc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
