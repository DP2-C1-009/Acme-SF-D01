
package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
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
		int managerId = super.getRequest().getPrincipal().getActiveRoleId();
		int totalCouldUserStories;
		int totalShouldUserStories;
		int totalMustUserStories;
		int totalWontUserStories;
		Double userStoryEstimatedCostAverage;
		Double userStoryEstimatedCostDeviation;
		Integer maximumUserStoryEstimatedCost;
		Integer minimumUserStoryEstimatedCost;
		Double projectCostAverage;
		Double projectCostDeviation;
		Integer maximumProjectCost;
		Integer minimumProjectCost;

		totalCouldUserStories = this.repository.totalCouldUserStories(managerId);
		totalShouldUserStories = this.repository.totalShouldUserStories(managerId);
		totalMustUserStories = this.repository.totalMustUserStories(managerId);
		totalWontUserStories = this.repository.totalWontUserStories(managerId);
		userStoryEstimatedCostAverage = this.repository.userStoryEstimatedCostAverage(managerId);
		userStoryEstimatedCostDeviation = this.repository.userStoryEstimatedCostDeviation(managerId);
		maximumUserStoryEstimatedCost = this.repository.maximumUserStoryEstimatedCost(managerId);
		minimumUserStoryEstimatedCost = this.repository.minimumUserStoryEstimatedCost(managerId);
		projectCostAverage = this.repository.projectCostAverage(managerId);
		projectCostDeviation = this.repository.projectCostDeviation(managerId);
		maximumProjectCost = this.repository.maximumProjectCost(managerId);
		minimumProjectCost = this.repository.minimumProjectCost(managerId);

		dashboard = new ManagerDashboard();
		dashboard.setTotalCouldUserStories(totalCouldUserStories);
		dashboard.setTotalShouldUserStories(totalShouldUserStories);
		dashboard.setTotalMustUserStories(totalMustUserStories);
		dashboard.setTotalWontUserStories(totalWontUserStories);
		dashboard.setUserStoryEstimatedCostAverage(userStoryEstimatedCostAverage);
		dashboard.setUserStoryEstimatedCostDeviation(userStoryEstimatedCostDeviation);
		dashboard.setMaximumUserStoryEstimatedCost(maximumUserStoryEstimatedCost);
		dashboard.setMinimumUserStoryEstimatedCost(minimumUserStoryEstimatedCost);
		dashboard.setProjectCostAverage(projectCostAverage);
		dashboard.setProjectCostDeviation(projectCostDeviation);
		dashboard.setMaximumProjectCost(maximumProjectCost);
		dashboard.setMinimumProjectCost(minimumProjectCost);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"totalCouldUserStories", "totalShouldUserStories", "totalMustUserStories", "totalWontUserStories", // 
			"userStoryEstimatedCostAverage", "userStoryEstimatedCostDeviation", "maximumUserStoryEstimatedCost", "minimumUserStoryEstimatedCost", //
			"projectCostAverage", "projectCostDeviation", "maximumProjectCost", "minimumProjectCost");

		super.getResponse().addData(dataset);
	}

}
