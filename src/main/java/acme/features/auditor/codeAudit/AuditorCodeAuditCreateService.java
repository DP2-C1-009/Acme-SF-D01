
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
import acme.entities.codeAudits.CodeAudit;
import acme.entities.codeAudits.CodeAuditType;
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditCreateService extends AbstractService<Auditor, CodeAudit> {

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
		Auditor auditor;
		Date moment;

		auditor = this.repository.findAuditorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new CodeAudit();
		object.setAuditor(auditor);

		moment = MomentHelper.getCurrentMoment();
		object.setExecution(moment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;
		int pId;
		Project project;

		pId = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(pId);
		object.setProject(project);
		super.bind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink");

	}

	@Override
	public void validate(final CodeAudit object) {
		Collection<String> allCodes = this.repository.findAllCodes();

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allCodes.contains(object.getCode()), "code", "client.audit.error.codeDuplicate");

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		object.setDraftMode(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		int id;

		id = super.getRequest().getPrincipal().getActiveRoleId();
		Collection<Project> projects = this.repository.findProjectsDraftModeFalse();
		SelectChoices choices = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "moreInfoLink");
		dataset.put("types", SelectChoices.from(CodeAuditType.class, object.getType()));
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);

	}

}
