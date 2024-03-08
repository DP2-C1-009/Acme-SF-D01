
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashBoard extends AbstractForm {

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

	private Double				averageCostProjects;
	private Double				deviationCostProjects;
	private Double				maxCostProjects;
	private Double				minCostProjects;

}
