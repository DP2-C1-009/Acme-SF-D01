
package acme.features.clients.contracts;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;
import acme.validators.MoneyValidator;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	@Autowired
	private ClientContractRepository	repository;

	@Autowired
	private MoneyValidator				moneyValidator;


	@Override
	public void authorise() {
		boolean status;
		Contract object;
		Principal principal;
		int contractId;

		contractId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(contractId);

		principal = super.getRequest().getPrincipal();

		status = object != null && object.getClient().getId() == principal.getActiveRoleId() && object.isDraftmode();

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
	public void bind(final Contract object) {
		assert object != null;

		super.bind(object, "code", "providerName", "customerName", "goals", "budget");

	}

	@Override
	public void validate(final Contract object) {
		boolean isCodeChanged = false;
		final Collection<String> allCodes = this.repository.findAllContractsCode();
		final Contract contract = this.repository.findOneContractById(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			if (object.getBudget().getAmount() < 0.0)
				super.state(false, "budget", "client.contract.error.negativeBudget");
			if (object.getBudget().getAmount() > 1000000.0)
				super.state(false, "budget", "client.contract.error.overLimit");
			super.state(this.moneyValidator.moneyValidator(object.getBudget().getCurrency()), "budget", "client.contract.error.moneyValidator");
			super.state(this.validatorProjectCost(object), "budget", "client.contract.error.projectBudgetTotal");
		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !contract.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "client.contract.error.codeDuplicate");
		}

	}

	private boolean validatorProjectCost(final Contract object) {
		assert object != null;
		double projectCost = object.getProject().getCost().getAmount();
		double totalCost = 0.0;
		if (object.getProject() != null) {
			Collection<Contract> allContracts = this.repository.findAllContractsWithProject(object.getProject().getId());
			for (Contract c : allContracts)
				if (!c.isDraftmode())
					totalCost = totalCost + c.getBudget().getAmount();

		}
		Boolean res;
		res = projectCost >= totalCost + object.getBudget().getAmount();
		return res;

	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		object.setDraftmode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;

		Project objectProject = object.getProject();

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftmode");
		dataset.put("projectCode", objectProject.getCode());

		super.getResponse().addData(dataset);

	}

}
