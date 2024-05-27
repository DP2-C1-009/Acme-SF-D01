
package acme.features.manager.madeOf;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		super.bind(object, "story", "work");
	}

	@Override
	public void validate(final MadeOf object) {
		assert object != null;
		Project project;
		UserStory userStory;

		project = object.getWork();
		userStory = object.getStory();
		int managerId = super.getRequest().getPrincipal().getActiveRoleId();
		Manager manager = this.repository.findOneManagerById(managerId);

		super.state(object.getWork() != null, "*", "manager.madeof.create.error.null-project");
		super.state(object.getStory() != null, "*", "manager.madeof.create.error.null-user-story");

		if (!super.getBuffer().getErrors().hasErrors("work") && !super.getBuffer().getErrors().hasErrors("story")) {
			MadeOf existing;
			existing = this.repository.findOneMadeOfByProjectIdAndUserStoryId(project.getId(), userStory.getId());
			super.state(project.getManager().equals(manager) && userStory.getManager().equals(manager), "*", "manager.madeof.form.error.wrong-manager");
			super.state(existing == null, "*", "manager.madeof.form.error.existing-project-madeof");
			super.state(project.isDraftMode() || !userStory.isDraftMode(), "work", "manager.madeof.form.error.published-project");
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

		Collection<UserStory> userStories;
		Collection<Project> projects;
		SelectChoices choicesUserStory;
		SelectChoices choicesProject;
		Dataset dataset;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		userStories = this.repository.findPublishedUserStoriesByManagerId(managerId, false);
		choicesUserStory = SelectChoices.from(userStories, "title", object.getStory());

		projects = this.repository.findNotPublishedProjectsByManagerId(managerId, true);
		choicesProject = SelectChoices.from(projects, "code", object.getWork());

		dataset = super.unbind(object, "story", "work");
		dataset.put("userStory", choicesUserStory.getSelected().getKey());
		dataset.put("userStories", choicesUserStory);
		dataset.put("project", choicesProject.getSelected().getKey());
		dataset.put("projects", choicesProject);

		super.getResponse().addData(dataset);
	}

}
