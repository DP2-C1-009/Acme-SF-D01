
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.datatypes.SponsorshipType;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipCreateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;

		Principal principal = super.getRequest().getPrincipal();

		if (principal.hasRole(Sponsor.class))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		Sponsor sponsor;

		sponsor = this.repository.findOneSponsorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Sponsorship();
		object.setSponsor(sponsor);
		object.setDraftMode(true);
		object.setMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(object, "code", "start", "end", "amount", "type", "email", "furtherInfo");
		object.setProject(project);

	}

	@Override
	public void validate(final Sponsorship object) {
		Project project = object.getProject();

		final Collection<String> allSponsorshipCodes = this.repository.findManySponsorshipCodes();

		if (project == null)
			super.state(false, "project", "sponsor.sponsorship.error.project");

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allSponsorshipCodes.contains(object.getCode()), "code", "sponsor.sponsorship.error.codeDuplicate");

		if (!super.getBuffer().getErrors().hasErrors("amount"))
			super.state(object.getAmount().getAmount() > 0, "amount", "sponsor.sponsorship.error.amount-not-positive");

		if (object.getStart() != null && object.getEnd() != null) {
			super.state(MomentHelper.isAfter(object.getEnd(), object.getStart()), "end", "sponsor.sponsorship.error.end-after-start");
			super.state(MomentHelper.isLongEnough(object.getStart(), object.getEnd(), 30, ChronoUnit.DAYS), "end", "sponsor.sponsorship.error.start-end-one-month");
		}

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		object.setDraftMode(true);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choicesProjects;
		SelectChoices choicesTypes;

		projects = this.repository.findManyPublishedProjects();
		choicesProjects = SelectChoices.from(projects, "code", object.getProject());
		choicesTypes = SelectChoices.from(SponsorshipType.class, object.getType());

		dataset = super.unbind(object, "code", "start", "end", "amount", "email", "furtherInfo", "draftMode");
		dataset.put("project", choicesProjects.getSelected().getKey());
		dataset.put("projects", choicesProjects);
		dataset.put("type", choicesTypes.getSelected().getKey());
		dataset.put("types", choicesTypes);

		super.getResponse().addData(dataset);
	}

}
