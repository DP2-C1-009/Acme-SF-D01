
package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.UserStoryPriority;
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
		int totalNumberOfMustUserStories;
		int totalNumberOfShouldUserStories;
		int totalNumberOfCouldUserStories;
		int totalNumberOfWontUserStories;

		Double averageEstimatedCostUserStories;
		Double deviationEstimatedCostUserStories;
		Double maxEstimatedCostUserStories;
		Double minEstimatedCostUserStories;

		Double averageCostProjects;
		Double deviationCostProjects;
		Double minCostProjects;
		Double maxCostProjects;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		totalNumberOfMustUserStories = this.repository.totalNumberOfPriorityUserStories(UserStoryPriority.MUST, managerId);
		totalNumberOfShouldUserStories = this.repository.totalNumberOfPriorityUserStories(UserStoryPriority.SHOULD, managerId);
		totalNumberOfCouldUserStories = this.repository.totalNumberOfPriorityUserStories(UserStoryPriority.COULD, managerId);
		totalNumberOfWontUserStories = this.repository.totalNumberOfPriorityUserStories(UserStoryPriority.WONT, managerId);

		averageEstimatedCostUserStories = this.repository.averageEstimatedCostUserStories(managerId);
		deviationEstimatedCostUserStories = this.repository.deviationEstimatedCostUserStories(managerId);
		minEstimatedCostUserStories = this.repository.minEstimatedCostUserStories(managerId);
		maxEstimatedCostUserStories = this.repository.maxEstimatedCostUserStories(managerId);

		averageCostProjects = this.repository.averageCostProjects(managerId);
		deviationCostProjects = this.repository.deviationCostProjects(managerId);
		minCostProjects = this.repository.minCostProjects(managerId);
		maxCostProjects = this.repository.maxCostProjects(managerId);

		dashboard = new ManagerDashboard();
		dashboard.setTotalNumberOfMustUserStories(totalNumberOfMustUserStories);
		dashboard.setTotalNumberOfShouldUserStories(totalNumberOfShouldUserStories);
		dashboard.setTotalNumberOfCouldUserStories(totalNumberOfCouldUserStories);
		dashboard.setTotalNumberOfWontUserStories(totalNumberOfWontUserStories);
		dashboard.setAverageEstimatedCostUserStories(averageEstimatedCostUserStories);
		dashboard.setDeviationEstimatedCostUserStories(deviationEstimatedCostUserStories);
		dashboard.setMinEstimatedCostUserStories(minEstimatedCostUserStories);
		dashboard.setMaxEstimatedCostUserStories(maxEstimatedCostUserStories);
		dashboard.setAverageCostProjects(averageCostProjects);
		dashboard.setDeviationCostProjects(deviationCostProjects);
		dashboard.setMinCostProjects(minCostProjects);
		dashboard.setMaxCostProjects(maxCostProjects);

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
			"averageCostProjects", //
			"deviationCostProjects", //
			"minCostProjects", //
			"maxCostProjects");

		super.getResponse().addData(dataset);
	}

}
