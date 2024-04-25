
package acme.features.auditor.auditorDashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AuditorDashboardRepository extends AbstractRepository {

	@Query("Select count (ca) from CodeAudit ca Where ca.type = acme.entities.codeAudits.CodeAuditType.STATIC and ca.auditor.id = :id and ca.draftMode = false")
	int totalStaticAudit(int id);

	@Query("Select count (ca) from CodeAudit ca Where ca.type = acme.entities.codeAudits.CodeAuditType.DYNAMIC and ca.auditor.id = :id and ca.draftMode = false")
	int totalDynamicAudit(int id);

	@Query("Select avg(Select count(ar)from AuditRecord ar Where ar.codeAudit.id = ca.id) from CodeAudit ca Where ca.auditor.id = :id and ca.draftMode = false")
	Double averageAuditRecord(int id);

	//	@Query("select stddev(cast(count(ar) as double)) from AuditRecord ar where ar.codeAudit.auditor.id = :id ar.codeAudit.draftMode = true group by ar.codeAudit")
	//	Double deviationAuditRecord(int id);

	@Query("select min(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id) from CodeAudit ca where ca.auditor.id = :id and ca.draftMode = false")
	Integer minimumAuditRecord(int id);

	@Query("select max(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id) from CodeAudit ca where ca.auditor.id = :id and ca.draftMode = false")
	Integer maximumAuditRecord(int id);

	@Query("select avg(time_to_sec(timediff(ar.finishMoment, ar.startMoment)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :id and ar.codeAudit.draftMode = false")
	Double averagePeriodAuditRecord(int id);

	@Query("select stddev(time_to_sec(timediff(ar.finishMoment, ar.startMoment)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :id and ar.codeAudit.draftMode = false")
	Double deviationPeriodAuditRecord(int id);

	@Query("select min(time_to_sec(timediff(ar.finishMoment, ar.startMoment)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :id and ar.codeAudit.draftMode = false")
	Double minumPeriodAuditRecord(int id);

	@Query("select max(time_to_sec(timediff(ar.finishMoment, ar.startMoment)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :id and ar.codeAudit.draftMode = false")
	Double maximumPeriodAuditRecord(int id);

}
