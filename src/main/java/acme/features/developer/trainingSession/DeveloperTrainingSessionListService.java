
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionListService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		TrainingModule object;
		Principal principal;
		int tmId;

		tmId = super.getRequest().getData("trainingModuleId", int.class);
		object = this.repository.findOneTrainingModuleById(tmId);
		principal = super.getRequest().getPrincipal();

		status = object.getDeveloper().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrainingSession> objects;
		int tmId;

		tmId = super.getRequest().getData("trainingModuleId", int.class);
		objects = this.repository.findManyTrainingSessionsByTrainingModuleId(tmId);

		super.getBuffer().addData(objects);
		super.getResponse().addGlobal("trainingModuleId", tmId);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startDateTime", "instructor");

		super.getResponse().addData(dataset);
	}

}
