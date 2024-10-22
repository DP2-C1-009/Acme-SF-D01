
package acme.entities.projects;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MadeOf", indexes = {
	@Index(columnList = "story_id", unique = false), @Index(columnList = "work_id", unique = false), @Index(columnList = "work_id, story_id", unique = false)
})
public class MadeOf extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private UserStory			story;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project				work;

}
