
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
public class SponsorInvoiceDeleteService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Sponsorship sponsorship;
		Principal principal;
		Invoice invoice;

		id = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(id);
		sponsorship = invoice.getSponsorship();

		principal = super.getRequest().getPrincipal();

		status = sponsorship.getSponsor().getId() == principal.getActiveRoleId() && invoice.isDraftMode() && invoice != null;

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
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "code", "dueDate", "quantity", "tax", "furtherInfo");
	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.delete(object);
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
