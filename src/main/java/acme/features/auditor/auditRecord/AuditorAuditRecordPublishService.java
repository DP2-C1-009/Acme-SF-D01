
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
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordPublishService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	private AuditorAuditRecordRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		int id;
		AuditRecord auditRecord;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		auditRecord = this.repository.findAuditRecordById(id);
		principal = super.getRequest().getPrincipal();

		status = auditRecord != null && auditRecord.getCodeAudit().getAuditor().getId() == principal.getActiveRoleId();

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
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "startMoment", "finishMoment", "mark", "moreInfoLink");

	}

	@Override
	public void validate(final AuditRecord object) {
		Collection<String> allCodes = this.repository.findAllCodes();
		int id = super.getRequest().getData("id", int.class);
		AuditRecord auditRecord = this.repository.findAuditRecordById(id);
		boolean isCodeChanged = false;

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
			isCodeChanged = !object.getCode().equals(auditRecord.getCode());
		super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "client.audit.error.codeDuplicate");
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startMoment", "finishMoment", "mark", "moreInfoLink", "draftMode");
		dataset.put("marks", SelectChoices.from(AuditRecordMark.class, object.getMark()));

		super.getResponse().addData(dataset);

	}

}
