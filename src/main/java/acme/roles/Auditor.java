
package acme.roles;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Auditor extends AbstractRole {

	protected static final long	serialVersionUID	= 1L;

	@NotBlank
	@NotNull
	@Length(max = 75)
	protected String			firm;

	@NotBlank
	@Column(unique = true)
	@Length(max = 25)
	protected String			professionalID;

	@NotBlank
	@NotNull
	@Length(max = 100)
	protected String			certifications;

	@URL
	protected String			moreInfoLink;
}
