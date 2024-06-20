
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	int							totalLogLessThan25;
	int							totalLogLessBetween25And50;
	int							totalLogLessBetween50And75;
	int							totalLogAbove75;

	Map<String, Double>			averageBudgetOfContracts;
	Map<String, Double>			deviationBudgetOfContracts;
	Map<String, Double>			minimunBudgetOfContracts;
	Map<String, Double>			maximumBudgetOfContracts;

}
