
package acme.features.manager.userStory;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.UserStory;
import acme.entities.projects.UserStoryPriority;
import acme.roles.Manager;

@Service
public class ManagerUserStoryCreateService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory userStory;
		Manager manager;

		Principal principal = super.getRequest().getPrincipal();
		manager = this.repository.findManagerById(principal.getActiveRoleId());

		userStory = new UserStory();
		userStory.setDraftMode(true);
		userStory.setManager(manager);

		super.getBuffer().addData(userStory);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;
		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "optionalLink");
	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("estimatedCost"))
			super.state(object.getEstimatedCost() > 0, "estimatedCost", "manager.userStory.form.error.negativeCost");
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		SelectChoices choices;

		choices = SelectChoices.from(UserStoryPriority.class, object.getPriority());

		Dataset dataset;

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "optionalLink");

		if (object.isDraftMode()) {

			final Locale local = super.getRequest().getLocale();
			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");

		} else
			dataset.put("draftMode", "No");

		dataset.put("priority", choices);

		super.getResponse().addData(dataset);
	}

}
