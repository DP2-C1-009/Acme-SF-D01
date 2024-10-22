
package acme.features.manager.project;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.features.manager.userStory.ManagerUserStoryRepository;
import acme.roles.Manager;

@Service
public class ManagerProjectPublishService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository	managerProjectRepository;

	@Autowired
	private ManagerUserStoryRepository	managerUserStoryRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;
		Manager manager;

		projectId = super.getRequest().getData("id", int.class);
		project = this.managerProjectRepository.findOneProjectById(projectId);

		manager = project == null ? null : project.getManager();
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.managerProjectRepository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "pAbstract", "fatalErrors", "cost", "optionalLink");

	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing;

			existing = this.managerProjectRepository.findOneProjectByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "manager.project.form.error.codeDuplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("fatalErrors"))
			super.state(object.isFatalErrors() == false, "fatalErrors", "manager.project.form.error.fatalErrors");

		Collection<UserStory> userStories = this.managerUserStoryRepository.findAllUserStoriesByProjectId(object.getId());

		final boolean draftUserStories = userStories.stream().anyMatch(x -> x.isDraftMode());
		final boolean noUserStories = userStories.isEmpty();

		super.state(!noUserStories, "*", "manager.project.form.error.userStories-empty");
		super.state(!draftUserStories, "*", "manager.project.form.error.userStories-draftMode");

		boolean conditionProject = object.isDraftMode();
		super.state(conditionProject, "*", "manager.project.publish.error.draft-mode");
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		object.setDraftMode(false);
		this.managerProjectRepository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "pAbstract", "fatalErrors", "cost", "optionalLink", "manager");

		if (object.isDraftMode()) {

			final Locale local = super.getRequest().getLocale();
			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");

		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

}
