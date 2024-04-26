
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeAudits.AuditRecord;
import acme.entities.codeAudits.CodeAudit;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("Select ar from AuditRecord ar Where ar.codeAudit.id = :id ")
	Collection<AuditRecord> findAuditRecordByCodeAudit(int id);

	@Query("Select ar from AuditRecord ar Where ar.id = :id")
	AuditRecord findAuditRecordById(int id);

	@Query("Select ca from CodeAudit ca Where ca.id =:id")
	CodeAudit findCodeAuditById(int id);

	@Query("Select ar.code from AuditRecord ar")
	Collection<String> findAllCodes();

}
