
package acme.features.manager.userStory;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryListService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		Principal principal = super.getRequest().getPrincipal();
		status = principal.hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int managerId;
		Collection<UserStory> userStories;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		userStories = this.repository.findAllUserStoriesByManagerId(managerId);

		super.getBuffer().addData(userStories);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "optionalLink");
		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");
		super.getResponse().addData(dataset);
	}

}
