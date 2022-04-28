package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

	List<Activity> findByAddressStateOrderByAddressCity(String state);

	List<Activity> findByAddressCityOrderByActivityName(String city);

}
