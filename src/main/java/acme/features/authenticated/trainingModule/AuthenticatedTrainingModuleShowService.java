
package acme.features.authenticated.trainingModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.training.DifficultyLevel;
import acme.entities.training.TrainingModule;

@Service
public class AuthenticatedTrainingModuleShowService extends AbstractService<Authenticated, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int tmId;
		TrainingModule tm;

		tmId = super.getRequest().getData("id", int.class);
		tm = this.repository.findOneTrainingModuleById(tmId);
		status = tm != null && super.getRequest().getPrincipal().hasRole(Authenticated.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		Project objectProject = object.getProject();

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "estimatedTotalTime", "draftMode");
		dataset.put("difficultyLevels", choices);
		dataset.put("projectCode", objectProject.getCode());

		super.getResponse().addData(dataset);
	}

}
