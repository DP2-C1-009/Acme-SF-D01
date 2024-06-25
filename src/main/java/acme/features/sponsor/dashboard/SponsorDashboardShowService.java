
package acme.features.sponsor.dashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class SponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		Principal principal = super.getRequest().getPrincipal();
		int id = principal.getAccountId();
		Sponsor sponsor = this.repository.findOneSponsorById(id);
		status = sponsor != null && principal.hasRole(Sponsor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Principal principal = super.getRequest().getPrincipal();
		int userAccountId = principal.getAccountId();
		SponsorDashboard sponsorDashboard = new SponsorDashboard();
		Collection<Sponsorship> sponsorships = this.repository.findAllSponsorshipsBySponsorId(userAccountId);
		Collection<Invoice> invoices = this.repository.findAllInvoicesBySponsorId(userAccountId);

		// Default values
		sponsorDashboard.setInvoicesTaxLessOrEq21(0);
		sponsorDashboard.setSponsorshipsWithLink(0);

		sponsorDashboard.setAverageSponsorshipsAmount(null);
		sponsorDashboard.setDeviationSponsorshipsAmount(null);
		sponsorDashboard.setMaximumSponsorshipsAmount(null);
		sponsorDashboard.setMinimumSponsorshipsAmount(null);

		sponsorDashboard.setAverageInvoicesQuantity(null);
		sponsorDashboard.setDeviationInvoicesQuantity(null);
		sponsorDashboard.setMaximumInvoicesQuantity(null);
		sponsorDashboard.setMinimumInvoicesQuantity(null);

		if (!sponsorships.isEmpty()) {
			sponsorDashboard.setSponsorshipsWithLink(this.repository.findSponsorshipsWithLink(userAccountId));

			sponsorDashboard.setAverageSponsorshipsAmount(this.repository.findAverageSponsorshipsAmount(userAccountId));
			sponsorDashboard.setDeviationSponsorshipsAmount(this.repository.findDeviationSponsorshipsAmount(userAccountId));
			sponsorDashboard.setMaximumSponsorshipsAmount(this.repository.findMaximumSponsorshipsAmount(userAccountId));
			sponsorDashboard.setMinimumSponsorshipsAmount(this.repository.findMinimumSponsorshipsAmount(userAccountId));
		}

		if (!invoices.isEmpty()) {
			sponsorDashboard.setInvoicesTaxLessOrEq21(this.repository.findInvoicesTaxLessOrEq21(userAccountId));
			sponsorDashboard.setAverageInvoicesQuantity(this.repository.findAverageInvoicesQuantity(userAccountId));
			sponsorDashboard.setDeviationInvoicesQuantity(this.repository.findDeviationInvoicesQuantity(userAccountId));
			sponsorDashboard.setMaximumInvoicesQuantity(this.repository.findMaximumInvoicesQuantity(userAccountId));
			sponsorDashboard.setMinimumInvoicesQuantity(this.repository.findMinimumInvoicesQuantity(userAccountId));
		}

		super.getBuffer().addData(sponsorDashboard);
	}

	@Override
	public void unbind(final SponsorDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "invoicesTaxLessOrEq21", "sponsorshipsWithLink", "averageSponsorshipsAmount", "deviationSponsorshipsAmount", "minimumSponsorshipsAmount", "maximumSponsorshipsAmount", "averageInvoicesQuantity",
			"deviationInvoicesQuantity", "minimumInvoicesQuantity", "maximumInvoicesQuantity");

		super.getResponse().addData(dataset);
	}

}
