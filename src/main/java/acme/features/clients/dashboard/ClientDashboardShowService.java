
package acme.features.clients.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.Client;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	@Autowired
	protected ClientDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Principal principal = super.getRequest().getPrincipal();
		int id = principal.getAccountId();

		Client client = this.repository.findOneClientByUserAccountId(id);
		status = client != null && principal.hasRole(Client.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Client client;
		ClientDashboard dashboard;
		Principal principal;
		int id;

		principal = super.getRequest().getPrincipal();
		id = principal.getAccountId();
		client = this.repository.findOneClientByUserAccountId(id);

		int totalLogLessThan25;
		int totalLogLessBetween25And50;
		int totalLogLessBetween50And75;
		int totalLogAbove75;
		Double averageBudgetOfContracts;
		Double deviationBudgetOfContracts;
		Double minimunBudgetOfContracts;
		Double maximumBudgetOfContracts;

		totalLogLessThan25 = this.repository.findTotalLogLessThan25(client);
		totalLogLessBetween25And50 = this.repository.findTotalLogLessBetween50And75(client);
		totalLogLessBetween50And75 = this.repository.findTotalLogLessBetween50And75(client);
		totalLogAbove75 = this.repository.findTotalLogAbove75(client);

		averageBudgetOfContracts = this.repository.findAverageBudgetOfContracts(client);
		deviationBudgetOfContracts = this.repository.findDeviationBudgetOfContracts(client);
		minimunBudgetOfContracts = this.repository.findMinimunBudgetOfContracts(client);
		maximumBudgetOfContracts = this.repository.findMaximumBudgetOfContracts(client);

		dashboard = new ClientDashboard();

		dashboard.setTotalLogLessThan25(totalLogLessThan25);
		dashboard.setTotalLogLessBetween25And50(totalLogLessBetween25And50);
		dashboard.setTotalLogLessBetween50And75(totalLogLessBetween50And75);
		dashboard.setTotalLogAbove75(totalLogAbove75);

		dashboard.setAverageBudgetOfContracts(averageBudgetOfContracts);
		dashboard.setDeviationBudgetOfContracts(deviationBudgetOfContracts);
		dashboard.setMinimunBudgetOfContracts(minimunBudgetOfContracts);
		dashboard.setMaximumBudgetOfContracts(maximumBudgetOfContracts);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "totalLogLessThan25", "totalLogLessBetween25And50", "totalLogLessBetween50And75", "totalLogAbove75", "averageBudgetOfContracts", "deviationBudgetOfContracts", "minimunBudgetOfContracts", "maximumBudgetOfContracts");
		super.getResponse().addData(dataset);
	}

}
