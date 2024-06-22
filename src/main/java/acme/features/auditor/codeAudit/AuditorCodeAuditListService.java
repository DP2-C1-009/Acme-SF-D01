
package acme.features.auditor.codeAudit;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditListService extends AbstractService<Auditor, CodeAudit> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService<Auditor, CodeAudit> ---------------------------


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {

		Collection<CodeAudit> collection;
		int id = super.getRequest().getPrincipal().getActiveRoleId();

		collection = this.repository.findCodeAuditByAuditor(id);

		super.getBuffer().addData(collection);
	}

	@Override
	public void unbind(final CodeAudit object) {

		assert object != null;
		Dataset dataset;
		String p = object.getProject().getTitle();

		dataset = super.unbind(object, "code", "execution", "type");
		dataset.put("project", p);
		final Locale local = super.getRequest().getLocale();
		String draftModeText;
		if (object.isDraftMode()) {
			if (local.equals(Locale.ENGLISH))
				draftModeText = "Yes";
			else
				draftModeText = "Sí";
			dataset.put("draftMode", draftModeText);
		} else
			dataset.put("draftMode", "No");
		super.getResponse().addData(dataset);

	}

}
