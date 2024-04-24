
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
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditShowService extends AbstractService<Auditor, CodeAudit> {

	@Autowired
	private AuditorCodeAuditRepository repository;


	@Override
	public void authorise() {
		boolean status;
		CodeAudit object;
		Principal principal;
		int codeAuditId;

		codeAuditId = super.getRequest().getData("id", int.class);
		object = this.repository.findCodeAuditById(codeAuditId);

		principal = super.getRequest().getPrincipal();

		status = object != null && object.getAuditor().getId() == principal.getActiveRoleId();

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
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		Project p = object.getProject();
		Collection<AuditRecord> auditRecords = this.repository.findAuditRecordsByCodeAudit(object.getId());

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink", "draftMode");
		dataset.put("project", p.getTitle());
		dataset.put("types", SelectChoices.from(CodeAuditType.class, object.getType()));
		dataset.put("mark", object.getMark(auditRecords));

		super.getResponse().addData(dataset);

	}

}
