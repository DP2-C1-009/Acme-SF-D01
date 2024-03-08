
package acme.entities.objective;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Objective extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@NotNull
	private Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@NotNull
	private ObjectivePriority	priority;

	private boolean				critical;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				durationStart;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				durationFinish;

	@URL
	private String				optionalLink;

	// Derived attributes -----------------------------------------------------

	//	@Transient
	//	private boolean isDurationValid() {
	//		if (!this.durationStart.after(this.instantiationMoment))
	//			return false;
	//
	//		if (!this.durationStart.before(this.durationFinish) && !this.durationFinish.after(this.durationStart))
	//			return false;
	//
	//		long time = this.durationFinish.getTime() - this.durationStart.getTime();
	//		return time >= 3600000; // 1 hour in milliseconds
	//	}

}
