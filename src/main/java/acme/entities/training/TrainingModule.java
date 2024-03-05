
package acme.entities.training;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingModule extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}-[0-9]{3}$")
	@NotNull
	protected String			code;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Past
	protected Date				creationMoment;

	@NotBlank
	@NotNull
	@Length(max = 100)
	protected String			details;

	@NotNull
	protected DifficultyLevel	difficultyLevel;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				updateMoment;

	@URL
	protected String			optionalLink;

	@Positive
	protected Double			estimatedTotalTime;

	// Relationships ----------------------------------------------------------

	//	@NotNull
	//	@Valid
	//	@ManyToOne(optional = false)
	//	private Project				project;
	//
	//	@NotNull
	//	@Valid
	//	@ManyToOne(optional = false)
	//	private Developer			developer;

}
