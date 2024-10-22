
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
public class ManagerMadeOfShowService extends AbstractService<Manager, MadeOf> {

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
	public void unbind(final MadeOf object) {
		assert object != null;

		Principal principal = super.getRequest().getPrincipal();
		int managerId = principal.getActiveRoleId();

		Collection<UserStory> userStories = this.repository.findUserStoriesByManagerId(managerId);
		SelectChoices userStoryChoices = SelectChoices.from(userStories, "title", object.getStory());

		Collection<Project> projects = this.repository.findProjectsByManagerId(managerId);
		SelectChoices projectChoices = SelectChoices.from(projects, "title", object.getWork());

		Dataset dataset = super.unbind(object, "work", "story");
		dataset.put("work", object.getWork().getId());
		dataset.put("projects", projectChoices);
		dataset.put("story", object.getStory().getId());
		dataset.put("userStories", userStoryChoices);
		super.getResponse().addData(dataset);
	}
}
