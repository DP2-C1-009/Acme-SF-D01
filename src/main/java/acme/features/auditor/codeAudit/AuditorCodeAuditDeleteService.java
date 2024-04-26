
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
public class AuditorCodeAuditDeleteService extends AbstractService<Auditor, CodeAudit> {

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

		super.bind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink");

	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;
		Collection<AuditRecord> auditRecords;

		auditRecords = this.repository.findAuditRecordsByCodeAudit(object.getId());
		this.repository.deleteAll(auditRecords);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		int id;

		id = super.getRequest().getPrincipal().getActiveRoleId();

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink");
		dataset.put("types", SelectChoices.from(CodeAuditType.class, object.getType()));

		super.getResponse().addData(dataset);

	}

}
