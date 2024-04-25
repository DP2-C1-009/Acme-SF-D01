
package acme.features.manager.madeOf;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.MadeOf;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerMadeOfListService extends AbstractService<Manager, MadeOf> {

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
		Collection<MadeOf> object;
		int id;

		id = super.getRequest().getPrincipal().getActiveRoleId();
		object = this.repository.findMadeOfsByManagerId(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final MadeOf object) {
		assert object != null;

		Project project;
		UserStory userStory;
		int madeOfId;
		Dataset dataset;

		madeOfId = object.getId();
		project = this.repository.findOneProjectByMadeOfId(madeOfId);
		userStory = this.repository.findOneUserStoryByMadeOfId(madeOfId);

		dataset = super.unbind(object, "story", "work");
		dataset.put("work", project.getCode());
		dataset.put("story", userStory.getTitle());

		super.getResponse().addData(dataset);
	}
}
