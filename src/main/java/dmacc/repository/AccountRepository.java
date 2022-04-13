package dmacc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	public Account findOneByEmail(String email);
}
