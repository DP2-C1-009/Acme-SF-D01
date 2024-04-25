
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.datatypes.SponsorshipType;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipUpdateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Sponsorship object;
		Principal principal;
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(sponsorshipId);

		principal = super.getRequest().getPrincipal();

		status = object != null && object.getSponsor().getId() == principal.getActiveRoleId() && object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		super.bind(object, "code", "moment", "start", "end", "amount", "email", "furtherInfo", "type", "project");
	}

	@Override
	public void validate(final Sponsorship object) {
		boolean isCodeChanged = false;
		final Collection<String> allSponsorshipCodes = this.repository.findManySponsorshipCodes();
		final Sponsorship tm = this.repository.findOneSponsorshipById(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !tm.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allSponsorshipCodes.contains(object.getCode()), "code", "sponsor.sponsorship.error.codeDuplicate");
		}

		/*
		 * if (object.getUpdateMoment() != null && !super.getBuffer().getErrors().hasErrors("updateMoment"))
		 * super.state(MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "sponsor.sponsorship.error.update-date-before");
		 */
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(SponsorshipType.class, object.getType());
		Project objectProject = object.getProject();

		dataset = super.unbind(object, "code", "moment", "start", "end", "amount", "email", "furtherInfo", "draftMode");
		dataset.put("projectCode", objectProject.getCode());
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
