
package acme.features.clients.contracts;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;
import acme.validators.MoneyValidator;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	@Autowired
	private ClientContractRepository	repository;

	@Autowired
	private MoneyValidator				moneyValidator;


	@Override
	public void authorise() {
		boolean status = false;

		Principal principal = super.getRequest().getPrincipal();

		if (principal.hasRole(Client.class))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void validate(final Contract object) {
		final Collection<String> allCodes = this.repository.findAllContractsCode();

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allCodes.contains(object.getCode()), "code", "client.contract.error.codeDuplicate");

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			if (object.getBudget().getAmount() < 0.0)
				super.state(false, "budget", "client.contract.error.negativeBudget");
			if (object.getBudget().getAmount() > 1000000.0)
				super.state(false, "budget", "client.contract.error.overLimit");
			super.state(this.moneyValidator.moneyValidator(object.getBudget().getCurrency()), "budget", "client.contract.error.moneyValidator");
		}
	}

	@Override
	public void load() {
		Contract object;
		Client client;
		Date moment;

		client = this.repository.findClientById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Contract();
		object.setClient(client);
		moment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(moment);
		super.getBuffer().addData(object);
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		Date moment;

		object.setDraftmode(true);
		moment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(moment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choices;
		Date moment = MomentHelper.getCurrentMoment();

		projects = this.repository.findAllProjectsWithoutDraftMode();
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftmode");
		dataset.put("instantiationMoment", moment);
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);

	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(projectId);

		super.bind(object, "code", "providerName", "customerName", "goals", "budget");
		object.setProject(project);
	}

}
