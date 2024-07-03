
package acme.features.manager.dashboard;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.projects.UserStoryPriority;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class ManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ManagerDashboard dashboard;
		int managerId;

		dashboard = new ManagerDashboard();
		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		int totalNumberOfMustUserStories;
		int totalNumberOfShouldUserStories;
		int totalNumberOfCouldUserStories;
		int totalNumberOfWontUserStories;

		double averageEstimatedCostUserStories;
		double deviationEstimatedCostUserStories;
		double minEstimatedCostUserStories;
		double maxEstimatedCostUserStories;

		Map<String, Double> averageProjectCostPerCurrency;
		Map<String, Double> maxProjectCostPerCurrency;
		Map<String, Double> minProjectCostPerCurrency;
		Map<String, Double> deviationProjectCostPerCurrency;

		List<SystemConfiguration> systemConfiguration = this.repository.findSystemConfiguration();
		List<String> supportedCurrencies = Stream.of(systemConfiguration.get(0).getAcceptedCurrency().split(",")).map(String::trim).toList();

		Collection<Project> projects = this.repository.findProjectsByManagerId(managerId).stream().filter(x -> !x.isDraftMode()).toList();
		Collection<Money> projectCosts = projects.stream().map(Project::getCost).toList();
		Map<String, List<Money>> projectCostsByCurrency = projectCosts.stream().collect(Collectors.groupingBy(Money::getCurrency));

		averageProjectCostPerCurrency = projectCostsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMedia(entry.getValue(), entry.getKey()).getAmount()));
		maxProjectCostPerCurrency = projectCostsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMaximo(entry.getValue(), entry.getKey()).getAmount()));
		minProjectCostPerCurrency = projectCostsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMinimo(entry.getValue(), entry.getKey()).getAmount()));
		deviationProjectCostPerCurrency = projectCostsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularDesviacion(entry.getValue(), entry.getKey()).getAmount()));

		totalNumberOfMustUserStories = this.repository.totalNumberOfPriorityUserStories(UserStoryPriority.MUST, managerId, false);
		totalNumberOfShouldUserStories = this.repository.totalNumberOfPriorityUserStories(UserStoryPriority.SHOULD, managerId, false);
		totalNumberOfCouldUserStories = this.repository.totalNumberOfPriorityUserStories(UserStoryPriority.COULD, managerId, false);
		totalNumberOfWontUserStories = this.repository.totalNumberOfPriorityUserStories(UserStoryPriority.WONT, managerId, false);
		averageEstimatedCostUserStories = this.repository.averageEstimatedCostUserStories(managerId, false);
		deviationEstimatedCostUserStories = this.repository.deviationEstimatedCostUserStories(managerId, false);
		minEstimatedCostUserStories = this.repository.minEstimatedCostUserStories(managerId, false);
		maxEstimatedCostUserStories = this.repository.maxEstimatedCostUserStories(managerId, false);

		dashboard.setTotalNumberOfMustUserStories(totalNumberOfMustUserStories);
		dashboard.setTotalNumberOfShouldUserStories(totalNumberOfShouldUserStories);
		dashboard.setTotalNumberOfCouldUserStories(totalNumberOfCouldUserStories);
		dashboard.setTotalNumberOfWontUserStories(totalNumberOfWontUserStories);
		dashboard.setAverageEstimatedCostUserStories(averageEstimatedCostUserStories);
		dashboard.setDeviationEstimatedCostUserStories(deviationEstimatedCostUserStories);
		dashboard.setMinEstimatedCostUserStories(minEstimatedCostUserStories);
		dashboard.setMaxEstimatedCostUserStories(maxEstimatedCostUserStories);
		dashboard.setAverageProjectCostPerCurrency(averageProjectCostPerCurrency);
		dashboard.setDeviationProjectCostPerCurrency(deviationProjectCostPerCurrency);
		dashboard.setMinProjectCostPerCurrency(minProjectCostPerCurrency);
		dashboard.setMaxProjectCostPerCurrency(maxProjectCostPerCurrency);

		dashboard.setSupportedCurrencies(supportedCurrencies);

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"totalNumberOfMustUserStories", //
			"totalNumberOfShouldUserStories", // 
			"totalNumberOfCouldUserStories", //
			"totalNumberOfWontUserStories", //
			"averageEstimatedCostUserStories", //
			"deviationEstimatedCostUserStories", //
			"minEstimatedCostUserStories", //
			"maxEstimatedCostUserStories", //
			"averageProjectCostPerCurrency", //
			"deviationProjectCostPerCurrency", //
			"minProjectCostPerCurrency", //
			"maxProjectCostPerCurrency", //
			"supportedCurrencies");

		super.getResponse().addData(dataset);
	}

	private Money calcularMedia(final Collection<Money> budgets, final String currency) {
		double sumaTotal = 0.0;
		int cuenta = 0;
		for (Money budget : budgets) {
			sumaTotal += budget.getAmount();
			cuenta++;
		}
		double media = cuenta > 0 ? sumaTotal / cuenta : Double.NaN;
		Money moneyFinal = new Money();
		moneyFinal.setCurrency(currency);
		moneyFinal.setAmount(media);
		return moneyFinal;
	}

	private Money calcularMaximo(final Collection<Money> budgets, final String currency) {
		double maximo = Double.NEGATIVE_INFINITY;
		for (Money budget : budgets)
			if (budget.getAmount() > maximo)
				maximo = budget.getAmount();
		double resultadoMaximo = budgets.isEmpty() ? Double.NaN : maximo;
		Money moneyFinal = new Money();
		moneyFinal.setCurrency(currency);
		moneyFinal.setAmount(resultadoMaximo);
		return moneyFinal;
	}

	private Money calcularMinimo(final Collection<Money> budgets, final String currency) {
		double minimo = Double.POSITIVE_INFINITY;
		for (Money budget : budgets)
			if (budget.getAmount() < minimo)
				minimo = budget.getAmount();
		double resultadoMinimo = budgets.isEmpty() ? Double.NaN : minimo;
		Money moneyFinal = new Money();
		moneyFinal.setCurrency(currency);
		moneyFinal.setAmount(resultadoMinimo);
		return moneyFinal;
	}

	private Money calcularDesviacion(final Collection<Money> budgets, final String currency) {
		double sumaTotal = 0.0;
		int cuenta = 0;
		for (Money budget : budgets) {
			sumaTotal += budget.getAmount();
			cuenta++;
		}
		double media = cuenta > 0 ? sumaTotal / cuenta : Double.NaN;

		double sumaDiferenciasCuadradas = 0.0;
		for (Money budget : budgets)
			sumaDiferenciasCuadradas += Math.pow(budget.getAmount() - media, 2);
		double varianza = cuenta > 0 ? sumaDiferenciasCuadradas / cuenta : Double.NaN;
		double desviacionEstandar = Math.sqrt(varianza);
		Money desviacion = new Money();
		desviacion.setCurrency(currency);
		desviacion.setAmount(desviacionEstandar);
		return desviacion;
	}

}
