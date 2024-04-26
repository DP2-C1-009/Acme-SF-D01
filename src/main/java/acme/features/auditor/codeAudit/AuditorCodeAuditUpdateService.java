
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudits.AuditRecord;
import acme.entities.codeAudits.CodeAudit;
import acme.entities.codeAudits.CodeAuditType;
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

		super.bind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink", "draftMode");

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
		dataset.put("project", object.getProject().getTitle());
		dataset.put("types", SelectChoices.from(CodeAuditType.class, object.getType()));
		dataset.put("mark", object.getMark(auditRecords));

		super.getResponse().addData(dataset);

	}

}
