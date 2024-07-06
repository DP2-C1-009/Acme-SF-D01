
package acme.features.manager.project;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Manager;

@Service
public class ManagerProjectCreateService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		Manager manager;

		Principal principal = super.getRequest().getPrincipal();
		manager = this.repository.findOneManagerById(principal.getActiveRoleId());

		object = new Project();
		object.setDraftMode(true);
		object.setManager(manager);

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

			existing = this.repository.findOneProjectByCode(object.getCode());
			super.state(existing == null, "code", "manager.project.form.error.codeDuplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			super.state(object.getCost().getAmount() >= 0, "cost", "manager.project.form.error.negativeCost");
			super.state(object.getCost().getAmount() <= 1000000.0, "cost", "manager.project.form.error.overLimit");

			List<SystemConfiguration> sc = this.repository.findSystemConfiguration();
			final boolean foundCurrency = Stream.of(sc.get(0).getAcceptedCurrency().split(",")).map(String::trim).anyMatch(c -> c.equals(object.getCost().getCurrency()));
			super.state(foundCurrency, "cost", "manager.project.form.error.currencyNotSupported");
		}
	}

	@Override
	public void perform(final Project object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "pAbstract", "fatalErrors", "cost", "optionalLink", "draftMode");

		if (object.isDraftMode()) {

			final Locale local = super.getRequest().getLocale();
			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");

		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

}
