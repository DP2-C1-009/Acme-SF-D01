
package acme.features.any.progessLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLog;

@Service
public class AnyProgressLogListService extends AbstractService<Any, ProgressLog> {

	@Autowired
	private AnyProgressLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Contract object;
		int contractId;

		contractId = super.getRequest().getData("contractId", int.class);
		object = this.repository.findOneContractById(contractId);

		status = object.isDraftmode() == false;
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
		dataset = super.unbind(object, "recordId", "completeness", "responsiblePerson");

		super.getResponse().addData(dataset);
	}

}
