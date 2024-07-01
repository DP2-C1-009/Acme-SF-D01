
package acme.features.manager.project;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeAudits.AuditRecord;
import acme.entities.codeAudits.CodeAudit;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLog;
import acme.entities.projects.MadeOf;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p")
	Collection<Project> findAllProject();

	@Query("select p from Project p where p.manager.id = :id")
	Collection<Project> findAllProjectByManagerId(int id);

	@Query("select m from Manager m where m.id = :id")
	Manager findOneManagerById(int id);

	@Query("select m from Manager m")
	List<Manager> findAllManager();

	@Query("select p from Project p where p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("select count(mo) from MadeOf mo where mo.story.draftMode = true and mo.work.id = :id")
	boolean isAnyUserStoryInDraftModeByProjectId(int id);

	@Query("select mo from MadeOf mo where mo.work.id = :id")
	Collection<MadeOf> findAllMadeOfByProjectId(int id);

	@Query("select us from UserStory us join MadeOf mo on us.id = mo.story.id where mo.work.id = :projectId")
	Collection<UserStory> findAllUserStoriesOfAProject(int projectId);

	@Query("select c from Contract c where c.project.id = :id")
	Collection<Contract> findAllContractsByProjectId(int id);
	@Query("select pl from ProgressLog pl where pl.contract.id = :id")
	Collection<ProgressLog> findAllProgressLogsByContractId(int id);

	@Query("select ss from Sponsorship ss where ss.project.id = :id")
	Collection<Sponsorship> findAllSponsorshipsByProjectId(int id);
	@Query("select i from Invoice i where i.sponsorship.id = :id")
	Collection<Invoice> findAllInvoicesBySponsorshipId(int id);

	@Query("select ca from CodeAudit ca where ca.project.id = :id")
	Collection<CodeAudit> findAllCodeAuditsByProjectId(int id);
	@Query("select ar from AuditRecord ar where ar.codeAudit.id = :id")
	Collection<AuditRecord> findAllAuditsRecordsByCodeAuditsId(int id);

	@Query("select tm from TrainingModule tm where tm.project.id = :id")
	Collection<TrainingModule> findAllTrainingModuleByProjectId(int id);
	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :id")
	Collection<TrainingSession> findAllTrainingSessionByTrainingModuleId(int id);

	@Query("select s from SystemConfiguration s")
	List<SystemConfiguration> findSystemConfiguration();

}
