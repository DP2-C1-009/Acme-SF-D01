
package acme.entities.codeAudits;

import java.beans.Transient;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.projects.Project;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CodeAudit extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^[A-Z]{1,3}-[0-9]{3}$", message = "{validation.CodeAuditCode}")
	protected String			code;

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@NotNull
	protected Date				execution;

	@NotNull
	protected CodeAuditType		type;

	@NotBlank
	@Length(max = 100)
	@NotNull
	private String				correctiveActions;

	@URL
	@Length(max = 255)
	protected String			moreInfoLink;

	private boolean				draftMode;


	// Derived attributes -----------------------------------------------------
	@Transient
	public AuditRecordMark getMark(final Collection<AuditRecord> records) {
		Map<AuditRecordMark, Integer> frecMap = new HashMap<>();
		for (AuditRecord r : records) {
			AuditRecordMark mark = r.getMark();
			frecMap.put(mark, frecMap.getOrDefault(mark, 0) + 1);
		}
		AuditRecordMark mode = null;
		int max = 0;
		for (Map.Entry<AuditRecordMark, Integer> entry : frecMap.entrySet())
			if (entry.getValue() > max) {
				max = entry.getValue();
				mode = entry.getKey();
			}

		return mode;
	}
	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Auditor	auditor;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project	project;

}
