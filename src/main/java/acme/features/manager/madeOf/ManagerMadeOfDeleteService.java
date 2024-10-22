
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
public class ManagerMadeOfDeleteService extends AbstractService<Manager, MadeOf> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerMadeOfRepository repository;

	// AbstractService<Manager, MadeOf> ---------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		MadeOf madeOf = this.repository.findOneMadeOfById(id);

		Principal principal = super.getRequest().getPrincipal();
		Manager manager = this.repository.findOneManagerById(principal.getActiveRoleId());

		boolean status = madeOf != null && super.getRequest().getPrincipal().hasRole(manager) && madeOf.getWork().getManager().equals(manager) && madeOf.getStory().getManager().equals(manager);

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
	}

	@Override
	public void perform(final MadeOf object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final MadeOf object) {
		assert object != null;

		Principal principal = super.getRequest().getPrincipal();
		int managerId = principal.getActiveRoleId();

		Collection<UserStory> userStories = this.repository.findUserStoriesByManagerId(managerId);
		SelectChoices userStoryChoices = SelectChoices.from(userStories, "title", object.getStory());

		Dataset dataset = new Dataset();
		dataset.put("storyId", userStoryChoices.getSelected().getKey());
		dataset.put("userStories", userStoryChoices);

		super.getResponse().addData(dataset);
	}
}
