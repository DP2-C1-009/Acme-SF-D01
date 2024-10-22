
package acme.features.manager.userStory;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryListMineService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		int projectId = super.getRequest().getData("projectId", int.class);
		Project project = this.repository.findOneProjectByProjectId(projectId);
		Principal principal = super.getRequest().getPrincipal();
		Manager manager = this.repository.findManagerById(principal.getActiveRoleId());

		boolean status = super.getRequest().getPrincipal().hasRole(manager) && project.getManager().equals(manager);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<UserStory> userStories;

		int projectId = super.getRequest().getData("projectId", int.class);
		userStories = this.repository.findAllUserStoriesByProjectId(projectId);

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
