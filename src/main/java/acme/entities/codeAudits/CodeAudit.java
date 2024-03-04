
package acme.entities.codeAudits;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CodeAudit extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	protected String			code;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				execution;

	protected CodeAuditType		type;

	@NotBlank
	@Length(max = 100)
	private String				correctiveActions;

	@URL
	protected String			moreInfoLink;
}
