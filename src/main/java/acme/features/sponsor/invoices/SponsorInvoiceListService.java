
package acme.features.sponsor.invoices;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceListService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Sponsorship object;
		Principal principal;
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		object = this.repository.findOneSponsorshipById(sponsorshipId);
		principal = super.getRequest().getPrincipal();

		status = object.getSponsor().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Invoice> objects;
		int sponsorshipId;
		boolean sponsorshipDraftMode;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		objects = this.repository.findManyInvoicesBySponsorshipId(sponsorshipId);
		sponsorshipDraftMode = this.repository.findSponsorshipDraftModeById(sponsorshipId);

		super.getBuffer().addData(objects);
		super.getResponse().addGlobal("sponsorshipId", sponsorshipId);
		super.getResponse().addGlobal("sponsorshipDraftMode", sponsorshipDraftMode);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "dueDate", "quantity", "tax");

		dataset.put("totalAmount", object.totalAmount());

		super.getResponse().addData(dataset);
	}

}
