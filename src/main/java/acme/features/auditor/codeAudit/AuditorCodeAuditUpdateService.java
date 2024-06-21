
package acme.features.auditor.codeAudit;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudits.AuditRecord;
import acme.entities.codeAudits.CodeAudit;
import acme.entities.codeAudits.CodeAuditType;
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditUpdateService extends AbstractService<Auditor, CodeAudit> {

	@Autowired
	private AuditorCodeAuditRepository repository;


	@Override
	public void authorise() {
		boolean status = false;

		Principal principal = super.getRequest().getPrincipal();

		if (principal.hasRole(Auditor.class))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCodeAuditById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "code", "project", "execution", "type", "correctiveActions", "moreInfoLink");

	}

	@Override
	public void validate(final CodeAudit object) {
		Collection<String> allCodes = this.repository.findAllCodes();
		boolean isCodeChanged = false;
		int id = super.getRequest().getData("id", int.class);
		CodeAudit codeAudit = this.repository.findCodeAuditById(id);

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !object.getCode().equals(codeAudit.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "client.audit.error.codeDuplicate");
		}
		if (!super.getBuffer().getErrors().hasErrors("execution")) {
			Date execution = object.getExecution();
			Date minDate = MomentHelper.parse("1999-12-31 23:59", "yyyy-MM-dd HH:mm");
			super.state(execution.after(minDate), "execution", "auditor.audit.error.minDate");
		}

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		int id;

		id = super.getRequest().getPrincipal().getActiveRoleId();
		Collection<AuditRecord> auditRecords = this.repository.findAuditRecordsByCodeAudit(object.getId());

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink", "draftMode");

		dataset.put("types", SelectChoices.from(CodeAuditType.class, object.getType()));
		dataset.put("mark", object.getMark(auditRecords));

		Collection<Project> projects = this.repository.findProjectsDraftModeFalse();
		SelectChoices choices = SelectChoices.from(projects, "code", object.getProject());

		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);

	}

}
