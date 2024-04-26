
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
public class ManagerMadeOfDeleteService extends AbstractService<Manager, MadeOf> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerMadeOfRepository repository;

	// AbstractService<Manager, MadeOf> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MadeOf madeOf;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		madeOf = this.repository.findOneMadeOfById(masterId);
		manager = madeOf == null ? null : this.repository.findOneManagerByMadeOfId(masterId);
		status = madeOf != null && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MadeOf object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneMadeOfById(id);

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
	}

	@Override
	public void perform(final MadeOf object) {
		assert object != null;

		this.repository.delete(object);
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

		userStories = this.repository.findUserStoriesByManagerId(managerId);
		choicesUserStory = SelectChoices.from(userStories, "title", object.getStory());

		projects = this.repository.findProjectsByManagerId(managerId);
		choicesProject = SelectChoices.from(projects, "code", object.getWork());

		dataset = super.unbind(object, "story", "work");
		dataset.put("story", choicesUserStory.getSelected().getKey());
		dataset.put("userStories", choicesUserStory);
		dataset.put("work", choicesProject.getSelected().getKey());
		dataset.put("projects", choicesProject);

		super.getResponse().addData(dataset);
	}
}
