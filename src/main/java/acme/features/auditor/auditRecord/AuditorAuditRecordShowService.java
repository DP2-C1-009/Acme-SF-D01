
package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudits.AuditRecord;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordShowService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	private AuditorAuditRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		AuditRecord object;
		Principal principal;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditRecordById(id);

		principal = super.getRequest().getPrincipal();

		status = object != null && object.getCodeAudit().getAuditor().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditRecordById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startMoment", "finishMoment", "mark", "moreInfoLink");

		super.getResponse().addData(dataset);

	}

}
