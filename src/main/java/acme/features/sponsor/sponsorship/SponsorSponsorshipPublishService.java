
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.datatypes.SponsorshipType;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

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

		status = object != null && object.isDraftMode() && object.getSponsor().getId() == principal.getActiveRoleId();

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
		assert object != null;

		final Collection<Invoice> invoices = this.repository.findManyInvoicesBySponsorshipId(object.getId());

		if (object.getAmount() != null) {
			super.state(!invoices.stream().anyMatch(i -> i.isDraftMode()), "draftMode", "sponsor.sponsorship.error.invoicesDraftMode");
			Double sponsorshipAlreadyPay = invoices.stream().filter(in -> in.getId() != object.getId()).map(Invoice::totalAmount).mapToDouble(Money::getAmount).sum();
			super.state(sponsorshipAlreadyPay.equals(object.getAmount().getAmount()), "draftMode", "sponsor.sponsorship.error.totalAmountInvoices");

		}

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		object.setDraftMode(false);
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

		dataset = super.unbind(object, "code", "moment", "start", "end", "amount", "email", "furtherInfo", "draftMode");
		dataset.put("project", choicesProjects.getSelected().getKey());
		dataset.put("projects", choicesProjects);
		dataset.put("type", choicesTypes.getSelected().getKey());
		dataset.put("types", choicesTypes);

		super.getResponse().addData(dataset);
	}

}
