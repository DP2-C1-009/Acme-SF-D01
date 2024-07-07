
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
public class SponsorInvoicePublishService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Sponsorship sponsorship = null;
		Principal principal;
		Invoice invoice;

		id = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(id);

		if (invoice != null)
			sponsorship = invoice.getSponsorship();

		principal = super.getRequest().getPrincipal();

		status = invoice != null && sponsorship.getSponsor().getId() == principal.getActiveRoleId() && invoice.isDraftMode();

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

		boolean isCodeChanged = false;
		final Collection<String> allInvoiceCodes = this.repository.findManyInvoiceCodes();
		final Invoice invoice = this.repository.findOneInvoiceById(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !invoice.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allInvoiceCodes.contains(object.getCode()), "code", "sponsor.invoice.error.codeDuplicate");
		}

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

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		Sponsorship sponsorship = object.getSponsorship();

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "furtherInfo", "draftMode");
		dataset.put("sponsorshipCode", sponsorship.getCode());
		dataset.put("sponsorshipId", sponsorship.getId());
		if (object.getQuantity() != null)
			dataset.put("totalAmount", object.totalAmount());

		super.getResponse().addData(dataset);
	}

}
