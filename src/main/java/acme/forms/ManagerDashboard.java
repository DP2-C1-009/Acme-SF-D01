
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private int					totalNumberOfMustUserStories;
	private int					totalNumberOfShouldUserStories;
	private int					totalNumberOfCouldUserStories;
	private int					totalNumberOfWontUserStories;

	private Double				averageEstimatedCostUserStories;
	private Double				deviationEstimatedCostUserStories;
	private Double				maxEstimatedCostUserStories;
	private Double				minEstimatedCostUserStories;

	private Map<String, Double>	averageProjectCostPerCurrency;
	private Map<String, Double>	deviationProjectCostPerCurrency;
	private Map<String, Double>	maxProjectCostPerCurrency;
	private Map<String, Double>	minProjectCostPerCurrency;

	private List<String>		supportedCurrencies;

}
