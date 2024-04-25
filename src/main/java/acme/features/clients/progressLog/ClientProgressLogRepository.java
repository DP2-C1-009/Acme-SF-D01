
package acme.features.clients.progressLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLog;

@Repository
public interface ClientProgressLogRepository extends AbstractRepository {

	@Query("select c from Contract c where c.id = :id")
	Contract findOneContractById(int id);

	@Query("select p from ProgressLog p where p.contract.id = :id")
	Collection<ProgressLog> findProgressLogByContractId(int id);

	@Query("Select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("Select p from ProgressLog p where p.id = :id")
	ProgressLog findProgressLogById(int id);

	@Query("Select p.recordId from ProgressLog p")
	Collection<String> findAllProgressLogCodes();

}
