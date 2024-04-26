
package acme.features.developer.trainingSession;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionShowService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		TrainingModule tm;
		TrainingSession ts;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		ts = this.repository.findOneTrainingSessionById(id);
		tm = this.repository.findOneTrainingModuleById(ts.getTrainingModule().getId());

		principal = super.getRequest().getPrincipal();

		status = tm.getDeveloper().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingSessionById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		TrainingModule tmObject = object.getTrainingModule();

		dataset = super.unbind(object, "code", "startDateTime", "endDateTime", "location", "instructor", "contactEmail", "optionalLink", "draftMode");
		dataset.put("trainingModuleCode", tmObject.getCode());

		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

}
