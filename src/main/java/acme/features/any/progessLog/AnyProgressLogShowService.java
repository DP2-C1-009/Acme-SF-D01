
package acme.features.any.progessLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLog;

@Service
public class AnyProgressLogShowService extends AbstractService<Any, ProgressLog> {

	@Autowired
	private AnyProgressLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Contract object;
		int contractId;

		contractId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractByProgressLogId(contractId);

		status = object.isDraftmode() == false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		int progressLog;

		progressLog = super.getRequest().getData("id", int.class);
		object = this.repository.findProgressLogById(progressLog);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");

		super.getResponse().addData(dataset);
	}

}
