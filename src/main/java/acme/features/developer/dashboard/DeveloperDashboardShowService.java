
package acme.features.developer.dashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		Principal principal = super.getRequest().getPrincipal();
		int id = principal.getAccountId();
		Developer developer = this.repository.findOneDeveloperById(id);
		status = developer != null && principal.hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Principal principal = super.getRequest().getPrincipal();
		int userAccountId = principal.getAccountId();
		DeveloperDashboard developerDashboard = new DeveloperDashboard();
		Collection<TrainingModule> tms = this.repository.findAllTrainingModulesByDeveloperId(userAccountId);
		Collection<TrainingSession> tss = this.repository.findAllTrainingSessionsByDeveloperId(userAccountId);

		developerDashboard.setTotalTrainingSessionsWithLink(0);
		developerDashboard.setTotalTrainingModulesWithUpdateMoment(0);
		developerDashboard.setAverageTrainingModulesTime(null);
		developerDashboard.setDeviatonTrainingModulesTime(null);
		developerDashboard.setMinimumTrainingModulesTime(null);
		developerDashboard.setMaximumTrainingModulesTime(null);

		if (!tms.isEmpty()) {
			developerDashboard.setTotalTrainingModulesWithUpdateMoment(this.repository.totalTrainingModulesWithUpdateMoment(userAccountId));
			developerDashboard.setAverageTrainingModulesTime(this.repository.averageTrainingModulesTime(userAccountId));
			developerDashboard.setDeviatonTrainingModulesTime(this.repository.deviatonTrainingModulesTime(userAccountId));
			developerDashboard.setMinimumTrainingModulesTime(this.repository.minimumTrainingModulesTime(userAccountId));
			developerDashboard.setMaximumTrainingModulesTime(this.repository.maximumTrainingModulesTime(userAccountId));
		}

		if (!tss.isEmpty())
			developerDashboard.setTotalTrainingSessionsWithLink(this.repository.totalTrainingSessionsWithLink(userAccountId));

		super.getBuffer().addData(developerDashboard);
	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"totalTrainingModulesWithUpdateMoment", "totalTrainingSessionsWithLink", // 
			"averageTrainingModulesTime", "deviatonTrainingModulesTime", //
			"minimumTrainingModulesTime", "maximumTrainingModulesTime");

		super.getResponse().addData(dataset);
	}

}
