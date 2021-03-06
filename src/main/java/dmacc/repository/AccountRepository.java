package dmacc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmacc.beans.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	public Account findOneByEmail(String email);

	public List<Account> findByAccountType(String string);
}
