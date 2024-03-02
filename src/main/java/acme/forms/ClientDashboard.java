
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	Integer						progressLogsWithCompletenessBelow25;
	Integer						progressLogsWithCompletenessBetween25And50;
	Integer						progressLogsWithCompletenessBetween50And75;
	Integer						progressLogsWithCompletenessAbove75;

	Double						averageBudgetOfContracts;
	Double						deviationBudgetOfContracts;
	Double						minimunBudgetOfContracts;
	Double						maximumBudgetOfContracts;

}
