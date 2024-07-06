
package acme.features.clients.contracts;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractShowService extends AbstractService<Client, Contract> {

	@Autowired
	private ClientContractRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Contract object;
		Principal principal;
		int contractId;

		contractId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(contractId);

		principal = super.getRequest().getPrincipal();

		status = object != null && object.getClient().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;

		Collection<Project> projects;
		SelectChoices choices;

		if (object.isDraftmode())
			projects = this.repository.findAllProjectsWithoutDraftMode();
		else {
			int contractId = object.getId();
			projects = this.repository.findOneProjectByContractId(contractId);
		}

		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftmode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);

	}

}
