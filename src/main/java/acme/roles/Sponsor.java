
package acme.roles;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sponsor extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@NotNull
	@Length(max = 75)
	private String				name;

	@NotBlank
	@NotNull
	@Length(max = 100)
	private String				expectedBenefits;

	@URL
	@Length(max = 255)
	private String				furtherInformation;

	@Email
	@Length(max = 255)
	private String				email;

}
