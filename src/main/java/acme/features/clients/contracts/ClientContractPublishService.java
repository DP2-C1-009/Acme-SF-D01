
package acme.features.clients.contracts;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

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

		super.bind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget");

	}

	@Override
	public void validate(final Contract object) {
		Project project = object.getProject();
		boolean isCodeChanged = false;

		double res = 0.0;

		final Collection<String> allCodes = this.repository.findAllContractsCode();
		final Collection<Contract> allContractsByProject = this.repository.findAllContractsWithProject(object.getProject().getId());
		final Contract contract = this.repository.findOneContractById(object.getId());
		List<String> validMoneyType = Arrays.asList("USD", "EUR", "GBP", "CHF", "JPY", "HKD", "CAD", "CNY", "AUD", "BRL", "RUB", "MXN");
		Money budget = object.getBudget();

		for (Contract c : allContractsByProject) {
			Double money = c.getBudget().getAmount();
			res = res + money;
		}

		if (budget != null) {
			res = res + budget.getAmount();
			if (project.getCost() < res)
				super.state(false, "budget", "client.contract.error.projectBudgetTotal");
			if (budget.getAmount() < 0.0)
				super.state(false, "budget", "client.contract.error.projectBudget");
			if (!validMoneyType.contains(object.getBudget().getCurrency()))
				super.state(false, "budget", "client.contract.error.negativeType");

		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !contract.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "client.contract.error.codeDuplicate");
		}

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
