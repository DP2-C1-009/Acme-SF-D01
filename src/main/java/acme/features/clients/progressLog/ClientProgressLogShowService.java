
package acme.features.clients.progressLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogShowService extends AbstractService<Client, ProgressLog> {

	@Autowired
	private ClientProgressLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Contract contract;
		ProgressLog progressLog;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);
		contract = this.repository.findContractById(progressLog.getContract().getId());

		principal = super.getRequest().getPrincipal();

		status = contract.getClient().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		ProgressLog progressLog;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);

		super.getBuffer().addData(progressLog);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;
		Contract objectContract = object.getContract();

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "draftmode");
		dataset.put("contractCode", objectContract.getCode());
		super.getResponse().addData(dataset);
	}

}
