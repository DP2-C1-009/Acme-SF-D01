
package acme.features.clients.progressLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogListService extends AbstractService<Client, ProgressLog> {

	@Autowired
	private ClientProgressLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Contract object;
		Principal principal;
		int contractId;

		contractId = super.getRequest().getData("contractId", int.class);
		object = this.repository.findOneContractById(contractId);
		principal = super.getRequest().getPrincipal();

		status = object.getClient().getId() == principal.getActiveRoleId() && object.isDraftmode() == false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ProgressLog> objects;
		int contractId;

		contractId = super.getRequest().getData("contractId", int.class);
		objects = this.repository.findProgressLogByContractId(contractId);

		super.getBuffer().addData(objects);
		super.getResponse().addGlobal("contractId", contractId);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "recordId", "completeness", "draftmode");

		super.getResponse().addData(dataset);
	}

}
