
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
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	@Query("select p from Project p where p.manager.id = :managerId")
	Collection<Project> findAllProjectsByManagerId(int managerId);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p where p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("select m from Manager m where m.id = :managerId")
	Manager findManagerById(int managerId);

	@Query("select mo from MadeOf mo where mo.work.id = :projectId")
	Collection<MadeOf> findAllMadeOfsByProjectId(int projectId);

	@Query("select c from CodeAudit c where c.project.id = :projectId")
	Collection<CodeAudit> findAllCodeAuditsFromProjectId(int projectId);

	@Query("select a from AuditRecord a where a.codeAudit.id = :codeAuditId")
	Collection<AuditRecord> findAllAuditRecordsFromCodeAuditId(int codeAuditId);

	@Query("select c from Contract c where c.project.id = :projectId")
	Collection<Contract> findAllContractsByProjectId(int projectId);

	@Query("select p from ProgressLog p where p.contract.id = :contractId")
	Collection<ProgressLog> findAllProgressLogsByContractId(int contractId);

	@Query("select s from Sponsorship s where s.project.id = :projectId")
	Collection<Sponsorship> findAllSponsorshipsByProjectId(int projectId);

	@Query("select i from Invoice i where i.sponsorship.id = :sponsorshipId")
	Collection<Invoice> findAllInvoicesBySponsorshipId(int sponsorshipId);

	@Query("select t from TrainingModule t where t.project.id = :projectId")
	Collection<TrainingModule> findAllTrainingModulesByProjectId(int projectId);

	@Query("select t from TrainingSession t where t.trainingModule.id = :trainingModuleId")
	Collection<TrainingSession> findAllTrainingSessionsFromTrainingModuleId(int trainingModuleId);

	@Query("select s from SystemConfiguration s")
	List<SystemConfiguration> findSystemConfiguration();
}
