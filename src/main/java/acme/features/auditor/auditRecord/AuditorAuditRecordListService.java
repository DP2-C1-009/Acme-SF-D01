
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudits.AuditRecord;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordListService extends AbstractService<Auditor, AuditRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService<Auditor, CodeAudit> ---------------------------


	@Override
	public void authorise() {
		boolean status = false;
		int codeAuditId;
		Principal principal;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findCodeAuditById(codeAuditId);

		principal = super.getRequest().getPrincipal();

		status = codeAudit.getAuditor().getId() == principal.getActiveRoleId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Collection<AuditRecord> collection;

		int id = super.getRequest().getData("codeAuditId", int.class);

		collection = this.repository.findAuditRecordByCodeAudit(id);

		super.getBuffer().addData(collection);
		super.getResponse().addGlobal("codeAuditId", id);

	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "mark", "moreInfoLink", "draftMode");

		super.getResponse().addData(dataset);
	}

}
