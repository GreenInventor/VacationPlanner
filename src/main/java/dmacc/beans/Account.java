package dmacc.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
	@Id
	@GeneratedValue
	private long id;
	@Column(unique=true)
	private String email;
	private String password;
	@Transient
	private String confirmPassword;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String accountType;
	private Address address;
}
