
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private int					totalNumberOfConsumers;
	private int					totalNumberOfProviders;
	private int					totalNumberOfDevelopers;
	private int					totalNumberOfSponsors;
	private int					totalNumberOfClients;
	private int					totalNumberOfAuditors;
	private int					totalNumberOfManagers;

	private Double				ratioOfNoticesWithEmailAndLink;

	private Double				ratioOfCriticalObjetives;
	private Double				ratioOfNonCriticalObjetives;

	private Double				averageValueOfRisks;
	private Double				deviationValueOfRisks;
	private Double				maxValueOfRisks;
	private Double				minValueOfRisks;

	private Double				averageNumberOfClaimsPostedOverTenWeeks;
	private Double				deviationNumberOfClaimsPostedOverTenWeeks;
	private Double				maxNumberOfClaimsPostedOverTenWeeks;
	private Double				minNumberOfClaimsPostedOverTenWeeks;

}
