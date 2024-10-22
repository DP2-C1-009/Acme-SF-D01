
package acme.features.manager.madeOf;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.MadeOf;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerMadeOfCreateService extends AbstractService<Manager, MadeOf> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerMadeOfRepository repository;

	// AbstractService<Manager, MadeOf> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MadeOf object;

		object = new MadeOf();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final MadeOf object) {
		assert object != null;

		int projectId = super.getRequest().getData("work", int.class);
		int userStoryId = super.getRequest().getData("story", int.class);

		Project work = this.repository.findOneProjectById(projectId);
		UserStory story = this.repository.findOneUserStoryById(userStoryId);

		object.setWork(work);
		object.setStory(story);

		super.bind(object, "story", "work");
	}

	@Override
	public void validate(final MadeOf object) {
		assert object != null;

		boolean nullProject = object.getWork() != null;
		super.state(nullProject, "*", "manager.made-of.create.error.null-project");

		boolean nullUserStory = object.getStory() != null;
		super.state(nullUserStory, "*", "manager.made-of.create.error.null-user-story");

		int managerId = super.getRequest().getPrincipal().getActiveRoleId();
		Manager manager = this.repository.findOneManagerById(managerId);

		if (nullProject && nullUserStory) {
			boolean condition = object.getWork().isDraftMode() && object.getWork().getManager().equals(manager) && object.getStory().getManager().equals(manager);
			super.state(condition, "*", "manager.made-of.create.error.draft-mode");
		}

	}

	@Override
	public void perform(final MadeOf object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final MadeOf object) {
		assert object != null;

		Principal principal = super.getRequest().getPrincipal();
		int managerId = principal.getActiveRoleId();

		Collection<Project> projects = this.repository.findUnpublishedProjectsByManagerId(managerId, true);
		SelectChoices projectChoices = SelectChoices.from(projects, "title", object.getWork());

		Collection<UserStory> userStories = this.repository.findPublishedUserStoriesByManagerId(managerId, false);
		SelectChoices userStoryChoices = SelectChoices.from(userStories, "title", object.getStory());

		Dataset dataset = super.unbind(object, "work", "story");

		dataset.put("work", projectChoices.getSelected().getKey());
		dataset.put("projects", projectChoices);
		dataset.put("story", userStoryChoices.getSelected().getKey());
		dataset.put("userStories", userStoryChoices);
		super.getResponse().addData(dataset);
	}

}
