
package acme.features.developer.trainingSession;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionPublishService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		TrainingModule tm = null;
		Principal principal;
		TrainingSession ts;

		id = super.getRequest().getData("id", int.class);
		ts = this.repository.findOneTrainingSessionById(id);

		if (ts != null)
			tm = ts.getTrainingModule();

		principal = super.getRequest().getPrincipal();

		status = ts != null && tm.getDeveloper().getId() == principal.getActiveRoleId() && ts.isDraftMode();

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
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startDateTime", "endDateTime", "location", "instructor", "contactEmail", "optionalLink");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;

		boolean isCodeChanged = false;
		final Collection<String> allTSCodes = this.repository.findManyTrainingSessionCodes();
		final TrainingSession ts = this.repository.findOneTrainingSessionById(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !ts.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allTSCodes.contains(object.getCode()), "code", "developer.training-session.error.codeDuplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("startDateTime")) {
			TrainingModule tm;
			int id;
			Date minimumStart;

			id = super.getRequest().getData("trainingModuleId", int.class);
			tm = this.repository.findOneTrainingModuleById(id);
			minimumStart = MomentHelper.deltaFromMoment(tm.getCreationMoment(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getStartDateTime(), minimumStart), "startDateTime", "developer.training-session.error.creation-moment-invalid");
		}

		if (!super.getBuffer().getErrors().hasErrors("endDateTime")) {
			Date minimumEnd;

			minimumEnd = MomentHelper.deltaFromMoment(object.getStartDateTime(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getEndDateTime(), minimumEnd), "endDateTime", "developer.training-session.error.too-close");
		}
	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		TrainingModule tmObject = object.getTrainingModule();

		dataset = super.unbind(object, "code", "startDateTime", "endDateTime", "location", "instructor", "contactEmail", "optionalLink", "draftMode");
		dataset.put("trainingModuleCode", tmObject.getCode());

		super.getResponse().addData(dataset);
	}

}
