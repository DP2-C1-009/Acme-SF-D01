
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							invoicesTaxLessOrEq21;

	int							sponsorshipsWithLink;

	double						averageSponsorshipsAmount;
	double						deviationSponsorshipsAmount;
	double						minimumSponsorshipsAmount;
	double						maximumSponsorshipsAmount;

	double						averageInvoicesQuantity;
	double						deviationInvoicesQuantity;
	double						minimumInvoicesQuantity;
	double						maximumInvoicesQuantity;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
