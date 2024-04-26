
package acme.features.sponsor.invoices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceShowService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Sponsorship sponsorship;
		Invoice invoice;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(id);
		sponsorship = this.repository.findOneSponsorshipById(invoice.getSponsorship().getId());

		principal = super.getRequest().getPrincipal();

		status = sponsorship.getSponsor().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneInvoiceById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		Sponsorship sponsorship = object.getSponsorship();

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "furtherInfo", "draftMode");
		dataset.put("sponsorshipCode", sponsorship.getCode());
		dataset.put("sponsorshipId", sponsorship.getId());
		dataset.put("totalAmount", object.totalAmount());

		super.getResponse().addData(dataset);
	}

}
