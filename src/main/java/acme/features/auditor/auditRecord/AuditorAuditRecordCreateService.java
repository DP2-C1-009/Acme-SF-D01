
package acme.features.auditor.auditRecord;

import java.time.temporal.ChronoUnit;
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
import acme.entities.codeAudits.AuditRecordMark;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordCreateService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	private AuditorAuditRecordRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		int id;

		Principal principal = super.getRequest().getPrincipal();

		if (principal.hasRole(Auditor.class))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		CodeAudit codeAudit;
		int id;

		id = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findCodeAuditById(id);

		object = new AuditRecord();
		object.setCodeAudit(codeAudit);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "startMoment", "finishMoment", "mark", "moreInfoLink");

	}

	@Override
	public void validate(final AuditRecord object) {
		Collection<String> allCodes = this.repository.findAllCodes();

		if (!super.getBuffer().getErrors().hasErrors("startMoment"))
			if (object.getFinishMoment() != null && object.getStartMoment() != null)
				super.state(MomentHelper.isAfter(object.getFinishMoment(), object.getStartMoment()), "startMoment", "validation.auditrecord.initialIsBefore");
		if (!super.getBuffer().getErrors().hasErrors("finishMoment"))
			if (object.getFinishMoment() != null && object.getStartMoment() != null) {
				Date end;
				end = MomentHelper.deltaFromMoment(object.getStartMoment(), 1, ChronoUnit.HOURS);
				super.state(MomentHelper.isAfterOrEqual(object.getFinishMoment(), end), "finishMoment", "validation.auditrecord.moment.minimun");
			}

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allCodes.contains(object.getCode()), "code", "client.audit.error.codeDuplicate");
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		object.setDraftMode(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		CodeAudit ca = object.getCodeAudit();

		dataset = super.unbind(object, "code", "startMoment", "finishMoment", "mark", "moreInfoLink");
		dataset.put("codeAuditId", ca.getId());
		dataset.put("marks", SelectChoices.from(AuditRecordMark.class, object.getMark()));

		super.getResponse().addData(dataset);

	}

}
