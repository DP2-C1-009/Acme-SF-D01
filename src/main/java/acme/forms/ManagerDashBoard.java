
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

	private double				averageEstimatedCostUserStories;
	private double				deviationEstimatedCostUserStories;
	private double				maxEstimatedCostUserStories;
	private double				minEstimatedCostUserStories;

	private double				averageCostProjects;
	private double				deviationCostProjects;
	private double				maxCostProjects;
	private double				minCostProjects;

}
