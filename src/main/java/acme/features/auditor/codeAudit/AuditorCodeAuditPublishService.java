
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudits.AuditRecord;
import acme.entities.codeAudits.AuditRecordMark;
import acme.entities.codeAudits.CodeAudit;
import acme.entities.codeAudits.CodeAuditType;
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditPublishService extends AbstractService<Auditor, CodeAudit> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorCodeAuditRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		CodeAudit object;
		Principal principal;
		int codeAuditId;

		codeAuditId = super.getRequest().getData("id", int.class);
		object = this.repository.findCodeAuditById(codeAuditId);

		principal = super.getRequest().getPrincipal();

		status = object != null && object.getAuditor().getId() == principal.getActiveRoleId() && object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit object;
		int codeAuditId;

		codeAuditId = super.getRequest().getData("id", int.class);
		object = this.repository.findCodeAuditById(codeAuditId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink");
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		Collection<String> allCodes = this.repository.findAllCodes();
		boolean isCodeChanged = false;
		int id = super.getRequest().getData("id", int.class);
		CodeAudit codeAudit = this.repository.findCodeAuditById(id);
		Collection<AuditRecord> auditRecords = this.repository.findAuditRecordsByCodeAudit(object.getId());

		for (AuditRecord ar : auditRecords)
			super.state(!ar.isDraftMode(), "code", "auditor.codeaudit.error.draftMode");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !object.getCode().equals(codeAudit.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "auditor.codeaudit.error.duplicated-code");
		}
		if (!super.getBuffer().getErrors().hasErrors("mark")) {
			AuditRecordMark mark = object.getMark(auditRecords);
			super.state(mark == AuditRecordMark.A || mark == AuditRecordMark.A_PLUS || mark == AuditRecordMark.B || mark == AuditRecordMark.C, "mark", "validation.codeAudit.mark.minimun");
		}

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesType;
		Collection<AuditRecord> auditRecords = this.repository.findAuditRecordsByCodeAudit(object.getId());
		Project p = object.getProject();

		choicesType = SelectChoices.from(CodeAuditType.class, object.getType());

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink", "draftMode");
		dataset.put("project", p.getTitle());
		dataset.put("types", choicesType);
		dataset.put("mark", object.getMark(auditRecords));

		super.getResponse().addData(dataset);
	}

}
