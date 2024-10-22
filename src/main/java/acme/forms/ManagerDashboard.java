
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	private int					totalCouldUserStories;
	private int					totalShouldUserStories;
	private int					totalMustUserStories;
	private int					totalWontUserStories;
	private Double				userStoryEstimatedCostAverage;
	private Double				userStoryEstimatedCostDeviation;
	private Integer				maximumUserStoryEstimatedCost;
	private Integer				minimumUserStoryEstimatedCost;
	private Double				projectCostAverage;
	private Double				projectCostDeviation;
	private Integer				maximumProjectCost;
	private Integer				minimumProjectCost;

}
