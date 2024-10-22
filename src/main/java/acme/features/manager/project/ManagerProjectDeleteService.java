
package acme.features.manager.project;

import java.util.Collection;
import java.util.Locale;

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
import acme.entities.projects.UserStory;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.features.manager.userStory.ManagerUserStoryRepository;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository	managerProjectRepository;

	@Autowired
	private ManagerUserStoryRepository	managerUserStoryRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Manager manager;
		Project project;

		projectId = super.getRequest().getData("id", int.class);
		project = this.managerProjectRepository.findOneProjectById(projectId);

		Principal principal = super.getRequest().getPrincipal();
		manager = this.managerProjectRepository.findManagerById(principal.getActiveRoleId());

		status = project != null && super.getRequest().getPrincipal().hasRole(manager) && project.getManager().equals(manager) && project.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.managerProjectRepository.findOneProjectById(id);

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
		boolean condition = object.isDraftMode();
		super.state(condition, "*", "manager.project.delete.error.draft-mode");
	}

	@Override
	public void perform(final Project object) {
		assert object != null;
		int projectId;
		projectId = super.getRequest().getData("id", int.class);

		// Check intermediate entity

		Collection<MadeOf> madeOfs;
		madeOfs = this.managerProjectRepository.findAllMadeOfsByProjectId(projectId);
		this.managerProjectRepository.deleteAll(madeOfs);

		// Check Code Audits

		Collection<CodeAudit> codeAudits = this.managerProjectRepository.findAllCodeAuditsFromProjectId(projectId);
		for (CodeAudit codeAudit : codeAudits) {
			Collection<AuditRecord> auditRecords = this.managerProjectRepository.findAllAuditRecordsFromCodeAuditId(codeAudit.getId());
			this.managerProjectRepository.deleteAll(auditRecords);
		}

		this.managerProjectRepository.deleteAll(codeAudits);

		// Check Contracts

		Collection<Contract> contracts = this.managerProjectRepository.findAllContractsByProjectId(projectId);
		for (Contract contract : contracts) {
			Collection<ProgressLog> progressLogs = this.managerProjectRepository.findAllProgressLogsByContractId(contract.getId());
			this.managerProjectRepository.deleteAll(progressLogs);
		}
		this.managerProjectRepository.deleteAll(contracts);

		// Check Sponsorship

		Collection<Sponsorship> sponsorships = this.managerProjectRepository.findAllSponsorshipsByProjectId(projectId);
		for (Sponsorship sponsorship : sponsorships) {
			Collection<Invoice> invoices = this.managerProjectRepository.findAllInvoicesBySponsorshipId(sponsorship.getId());
			this.managerProjectRepository.deleteAll(invoices);
		}
		this.managerProjectRepository.deleteAll(sponsorships);

		// Check Training Modules

		Collection<TrainingModule> trainingModules = this.managerProjectRepository.findAllTrainingModulesByProjectId(projectId);
		for (TrainingModule trainingModule : trainingModules) {
			Collection<TrainingSession> trainingSessions = this.managerProjectRepository.findAllTrainingSessionsFromTrainingModuleId(trainingModule.getId());
			this.managerProjectRepository.deleteAll(trainingSessions);
		}
		this.managerProjectRepository.deleteAll(trainingModules);

		// Check User Stories

		Collection<UserStory> userStories;
		userStories = this.managerUserStoryRepository.findAllUserStoriesByProjectId(projectId);
		this.managerUserStoryRepository.deleteAll(userStories);

		this.managerProjectRepository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "title", "pAbstract", "fatalErrors", "cost", "optionalLink");

		if (object.isDraftMode()) {

			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");

		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

}
