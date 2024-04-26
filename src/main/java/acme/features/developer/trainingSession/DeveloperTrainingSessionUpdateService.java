
package acme.features.developer.trainingSession;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

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
public class DeveloperTrainingSessionUpdateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		TrainingModule tm = null;
		TrainingSession ts;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		ts = this.repository.findOneTrainingSessionById(id);

		if (ts != null)
			tm = this.repository.findOneTrainingModuleById(ts.getTrainingModule().getId());

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
		boolean isCodeChanged = false;
		final Collection<String> allTSCodes = this.repository.findManyTrainingSessionCodes();
		final TrainingSession ts = this.repository.findOneTrainingSessionById(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !ts.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allTSCodes.contains(object.getCode()), "code", "developer.training-session.error.codeDuplicate");
		}

		Date maxStartDate = MomentHelper.deltaFromMoment(MomentHelper.parse("2201/01/01", "yyyy/MM/dd"), -7, ChronoUnit.DAYS);

		if (!super.getBuffer().getErrors().hasErrors("startDateTime")) {
			TrainingModule tm;
			int id;

			Date minimumStart;

			id = super.getRequest().getData("id", int.class);
			tm = this.repository.findOneTrainingModuleByTrainingSessionId(id);
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

		this.repository.save(object);
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
