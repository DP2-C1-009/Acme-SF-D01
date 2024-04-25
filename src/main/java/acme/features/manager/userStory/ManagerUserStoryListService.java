
package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.MadeOf;
import acme.entities.projects.Project;
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
		int projectId;
		Project project;
		int managerId;

		projectId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findProjectById(projectId);

		Principal principal = super.getRequest().getPrincipal();
		managerId = principal.getActiveRoleId();

		status = project != null && principal.hasRole(Manager.class) && project.getManager().getId() == managerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<UserStory> userStories;
		int projectId;

		projectId = super.getRequest().getData("masterId", int.class);
		userStories = this.repository.findMadeOfByProjectId(projectId).stream().map(MadeOf::getStory).toList();

		super.getBuffer().addData(userStories);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;
		int masterId;

		dataset = super.unbind(object, "title", "estimatedCost", "priority");
		masterId = super.getRequest().getData("masterId", int.class);

		super.getResponse().addGlobal("masterId", masterId);

		super.getResponse().addData(dataset);
	}

}
