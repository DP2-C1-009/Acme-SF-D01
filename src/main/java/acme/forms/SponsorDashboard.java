
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

	Double						averageSponsorshipsAmount;
	Double						deviationSponsorshipsAmount;
	Double						minimumSponsorshipsAmount;
	Double						maximumSponsorshipsAmount;

	Double						averageInvoicesQuantity;
	Double						deviationInvoicesQuantity;
	Double						minimumInvoicesQuantity;
	Double						maximumInvoicesQuantity;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
