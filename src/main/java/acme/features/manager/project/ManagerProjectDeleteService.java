
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudits.AuditRecord;
import acme.entities.codeAudits.CodeAudit;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLog;
import acme.entities.projects.MadeOf;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Manager manager1;
		Manager manager2;
		Project project;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);

		Principal principal = super.getRequest().getPrincipal();
		manager1 = this.repository.findOneManagerById(principal.getActiveRoleId());
		manager2 = project == null ? null : project.getManager();

		status = project != null && project.isDraftMode() && manager1.equals(manager2);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

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
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		Collection<Contract> contracts;
		Collection<ProgressLog> progressLogs;

		Collection<CodeAudit> codeAudits;
		Collection<AuditRecord> auditRecords;

		Collection<TrainingModule> trainingModules;
		Collection<TrainingSession> trainingSessions;

		Collection<Sponsorship> sponsorships;
		Collection<Invoice> invoices;

		Collection<MadeOf> madeOfs;
		int id = object.getId();

		// Contracts & ProgressLogs ---------------------------------------------------------

		contracts = this.repository.findAllContractsByProjectId(id);
		if (contracts != null)
			for (final Contract c : contracts) {
				progressLogs = this.repository.findAllProgressLogsByContractId(c.getId());
				this.repository.deleteAll(progressLogs);
			}

		// CodeAudits & AuditRecords ---------------------------------------------------------

		codeAudits = this.repository.findAllCodeAuditsByProjectId(id);
		if (codeAudits != null)
			for (final CodeAudit ca : codeAudits) {
				auditRecords = this.repository.findAllAuditsRecordsByCodeAuditsId(ca.getId());
				this.repository.deleteAll(auditRecords);
			}

		// TrainingModule & TrainingSession --------------------------------------------------

		trainingModules = this.repository.findAllTrainingModuleByProjectId(id);
		if (trainingModules != null)
			for (final TrainingModule tm : trainingModules) {
				trainingSessions = this.repository.findAllTrainingSessionByTrainingModuleId(tm.getId());
				this.repository.deleteAll(trainingSessions);
			}

		// Sponsorships & Invoices --------------------------------------------------

		sponsorships = this.repository.findAllSponsorshipsByProjectId(id);
		if (sponsorships != null)
			for (final Sponsorship ss : sponsorships) {
				invoices = this.repository.findAllInvoicesBySponsorshipId(ss.getId());
				this.repository.deleteAll(invoices);
			}

		// MadeOfs --------------------------------------------------

		madeOfs = this.repository.findAllMadeOfByProjectId(id);

		this.repository.deleteAll(madeOfs);
		this.repository.deleteAll(contracts);
		this.repository.deleteAll(codeAudits);
		this.repository.deleteAll(trainingModules);
		this.repository.deleteAll(sponsorships);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "pAbstract", "fatalErrors", "cost", "optionalLink", "draftMode");

		super.getResponse().addData(dataset);
	}

}
