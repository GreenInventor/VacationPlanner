package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
	public List<Car> findByDealershipAddressStateOrderByDealershipAddressCity(String state);

	public List<Car> findByDealershipAddressCityOrderByDealershipName(String city);
}
