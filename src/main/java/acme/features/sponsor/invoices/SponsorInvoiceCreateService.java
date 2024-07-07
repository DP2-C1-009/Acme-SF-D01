
package acme.features.sponsor.invoices;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceCreateService extends AbstractService<Sponsor, Invoice> {

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

		id = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(id);

		principal = super.getRequest().getPrincipal();

		status = sponsorship.getSponsor().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Invoice invoice;
		Sponsorship sponsorship;

		id = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(id);
		invoice = new Invoice();

		invoice.setSponsorship(sponsorship);
		invoice.setDraftMode(true);
		invoice.setRegistrationTime(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(invoice);
	}

	@Override
	public void bind(final Invoice object) {
		assert object != null;

		int id;
		Sponsorship sponsorship;

		id = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(id);

		super.bind(object, "code", "dueDate", "quantity", "tax", "furtherInfo");
		object.setSponsorship(sponsorship);
	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;

		final Collection<String> allCodes = this.repository.findManyInvoiceCodes();
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allCodes.contains(object.getCode()), "code", "sponsor.invoice.error.codeDuplicate");

		if (object.getRegistrationTime() != null && object.getDueDate() != null) {
			super.state(MomentHelper.isAfter(object.getDueDate(), object.getRegistrationTime()), "dueDate", "sponsor.invoice.error.due-after-registration");
			super.state(MomentHelper.isLongEnough(object.getRegistrationTime(), object.getDueDate(), 30, ChronoUnit.DAYS), "dueDate", "sponsor.invoice.error.registration-due-one-month");
		}

		if (object.getQuantity() != null)
			super.state(object.getQuantity().getAmount() > 0, "quantity", "sponsor.invoice.error.quantityzero");

		// Currency match
		final Collection<Invoice> invoices = this.repository.findManyInvoicesBySponsorshipId(object.getSponsorship().getId());
		if (object.getQuantity() != null) {
			String currency = invoices.stream().filter(inv -> !inv.isDraftMode()).map(inv -> inv.getQuantity().getCurrency()).findFirst().orElse(null);
			super.state(currency != null && currency.equals(object.getQuantity().getCurrency()), "quantity", "sponsor.sponsorship.error.currency-match");

		}
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		object.setDraftMode(true);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		Sponsorship sponsorship = object.getSponsorship();

		dataset = super.unbind(object, "code", "dueDate", "quantity", "tax", "furtherInfo", "draftMode");
		dataset.put("sponsorshipId", sponsorship.getId());
		dataset.put("sponsorshipCode", sponsorship.getCode());
		if (object.getQuantity() != null)
			dataset.put("totalAmount", object.totalAmount());

		super.getResponse().addData(dataset);
	}

}
