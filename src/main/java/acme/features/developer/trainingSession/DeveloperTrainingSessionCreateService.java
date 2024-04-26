
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
public class DeveloperTrainingSessionCreateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		TrainingModule tm;
		Principal principal;

		id = super.getRequest().getData("trainingModuleId", int.class);
		tm = this.repository.findOneTrainingModuleById(id);

		principal = super.getRequest().getPrincipal();

		status = tm.getDeveloper().getId() == principal.getActiveRoleId() && tm.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		TrainingSession ts;
		TrainingModule tm;

		id = super.getRequest().getData("trainingModuleId", int.class);
		tm = this.repository.findOneTrainingModuleById(id);
		ts = new TrainingSession();

		ts.setTrainingModule(tm);
		ts.setDraftMode(true);

		super.getBuffer().addData(ts);
	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		int id;
		TrainingModule tm;

		id = super.getRequest().getData("trainingModuleId", int.class);
		tm = this.repository.findOneTrainingModuleById(id);

		super.bind(object, "code", "startDateTime", "endDateTime", "location", "instructor", "contactEmail", "optionalLink");
		object.setTrainingModule(tm);
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;

		final Collection<String> allCodes = this.repository.findManyTrainingSessionCodes();
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allCodes.contains(object.getCode()), "code", "developer.training-session.error.codeDuplicate");

		Date maxStartDate = MomentHelper.deltaFromMoment(MomentHelper.parse("2201/01/01", "yyyy/MM/dd"), -7, ChronoUnit.DAYS);

		if (!super.getBuffer().getErrors().hasErrors("startDateTime")) {
			TrainingModule tm;
			int id;
			Date minimumStart;

			id = super.getRequest().getData("trainingModuleId", int.class);
			tm = this.repository.findOneTrainingModuleById(id);
			minimumStart = MomentHelper.deltaFromMoment(tm.getCreationMoment(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getStartDateTime(), minimumStart), "startDateTime", "developer.training-session.error.creation-moment-invalid");

			super.state(!MomentHelper.isAfterOrEqual(object.getStartDateTime(), maxStartDate), "startDateTime", "developer.training-session.error.start-date-too-far");
		}

		Date maxEndDate = MomentHelper.parse("2201/01/01", "yyyy/MM/dd");

		if (!super.getBuffer().getErrors().hasErrors("endDateTime")) {
			if (object.getStartDateTime() != null) {
				Date minimumEnd;

				minimumEnd = MomentHelper.deltaFromMoment(object.getStartDateTime(), 7, ChronoUnit.DAYS);
				super.state(MomentHelper.isAfterOrEqual(object.getEndDateTime(), minimumEnd), "endDateTime", "developer.training-session.error.too-close");
			}

			super.state(!MomentHelper.isAfterOrEqual(object.getEndDateTime(), maxEndDate), "endDateTime", "developer.training-session.error.end-date-too-far");
		}
	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		object.setDraftMode(true);

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;
		TrainingModule tmObject = object.getTrainingModule();

		dataset = super.unbind(object, "code", "startDateTime", "endDateTime", "location", "instructor", "contactEmail", "optionalLink", "draftMode");
		dataset.put("trainingModuleCode", tmObject.getCode());
		dataset.put("trainingModuleId", tmObject.getId());
		super.getResponse().addData(dataset);
	}

}
