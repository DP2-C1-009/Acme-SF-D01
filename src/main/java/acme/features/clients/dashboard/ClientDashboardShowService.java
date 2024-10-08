
package acme.features.clients.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.Client;
import acme.validators.MoneyValidatorRepository;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	@Autowired
	protected ClientDashboardRepository	repository;

	@Autowired
	protected MoneyValidatorRepository	moneyValidator;


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
		String money = this.moneyValidator.findAcceptedCurrencies();
		String[] currenciesArray = money.split(",\\s*");
		List<String> currenciesList = new ArrayList<>(Arrays.asList(currenciesArray));

		int totalLogLessThan25;
		int totalLogLessBetween25And50;
		int totalLogLessBetween50And75;
		int totalLogAbove75;

		totalLogLessThan25 = this.repository.findTotalLogLessThan25(client);
		totalLogLessBetween25And50 = this.repository.findTotalLogLessBetween50And75(client);
		totalLogLessBetween50And75 = this.repository.findTotalLogLessBetween50And75(client);
		totalLogAbove75 = this.repository.findTotalLogAbove75(client);

		Map<String, Double> averagePerCurrency = this.calculateAveragePerCurrency(client, currenciesList);
		Map<String, Double> deviationPerCurrency = this.calculateDeviationPerCurrency(client, currenciesList);
		Map<String, Double> minimunBudgetOfContracts = this.calculateMinimunBudgetPerCurrency(client, currenciesList);
		Map<String, Double> maximumBudgetOfContracts = this.calculateMaximumBudgetPerCurrency(client, currenciesList);

		dashboard = new ClientDashboard();

		dashboard.setTotalLogLessThan25(totalLogLessThan25);
		dashboard.setTotalLogLessBetween25And50(totalLogLessBetween25And50);
		dashboard.setTotalLogLessBetween50And75(totalLogLessBetween50And75);
		dashboard.setTotalLogAbove75(totalLogAbove75);

		dashboard.setAverageBudgetOfContracts(averagePerCurrency);
		dashboard.setDeviationBudgetOfContracts(deviationPerCurrency);
		dashboard.setMinimunBudgetOfContracts(minimunBudgetOfContracts);
		dashboard.setMaximumBudgetOfContracts(maximumBudgetOfContracts);

		super.getBuffer().addData(dashboard);
	}

	private Map<String, Double> calculateAveragePerCurrency(final Client client, final List<String> currencies) {
		Map<String, Double> averagePerCurrency = new HashMap<>();
		for (String currency : currencies) {
			Double averageBudgetOfContracts = this.repository.findAverageBudgetOfContracts(client, currency);
			if (averageBudgetOfContracts != null)
				averagePerCurrency.put(currency, averageBudgetOfContracts);
			else
				averagePerCurrency.put(currency, null);
		}
		return averagePerCurrency;
	}

	private Map<String, Double> calculateDeviationPerCurrency(final Client client, final List<String> currencies) {
		Map<String, Double> deviationPerCurrency = new HashMap<>();
		for (String currency : currencies) {
			Double deviationBudgetPerCurrency = this.repository.findDeviationBudgetOfContracts(client, currency);
			if (deviationBudgetPerCurrency != null)
				deviationPerCurrency.put(currency, deviationBudgetPerCurrency);
			else
				deviationPerCurrency.put(currency, null);

		}
		return deviationPerCurrency;
	}

	private Map<String, Double> calculateMinimunBudgetPerCurrency(final Client client, final List<String> currencies) {
		Map<String, Double> minimunBudgetOfContracts = new HashMap<>();
		for (String currency : currencies) {
			Double minContract = this.repository.findMinimunBudgetOfContracts(client, currency);
			if (minContract != null)
				minimunBudgetOfContracts.put(currency, minContract);
			else
				minimunBudgetOfContracts.put(currency, null);
		}
		return minimunBudgetOfContracts;
	}

	private Map<String, Double> calculateMaximumBudgetPerCurrency(final Client client, final List<String> currencies) {
		Map<String, Double> maximumBudgetOfContracts = new HashMap<>();
		for (String currency : currencies) {
			Double maxContract = this.repository.findMaximumBudgetOfContracts(client, currency);
			if (maxContract != null)
				maximumBudgetOfContracts.put(currency, maxContract);
			else
				maximumBudgetOfContracts.put(currency, null);
		}
		return maximumBudgetOfContracts;
	}

	@Override
	public void unbind(final ClientDashboard object) {
		assert object != null;
		Dataset dataset;
		String nullValues;
		Locale local;

		local = super.getRequest().getLocale();
		nullValues = local.equals(Locale.ENGLISH) ? "No Data" : "Sin Datos";

		String money = this.moneyValidator.findAcceptedCurrencies();
		String[] currenciesArray = money.split(",\\s*");

		dataset = super.unbind(object, "totalLogLessThan25", "totalLogLessBetween25And50", "totalLogLessBetween50And75", "totalLogAbove75", "averageBudgetOfContracts", "deviationBudgetOfContracts", "minimunBudgetOfContracts", "maximumBudgetOfContracts");
		dataset.put("nullValues", nullValues);

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("currency", currenciesArray);
	}

}
