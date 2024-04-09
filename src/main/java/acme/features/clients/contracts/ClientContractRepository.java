
package acme.features.clients.contracts;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.progressLogs.ProgressLog;
import acme.entities.projects.Project;
import acme.roles.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.client.id = :id")
	Collection<Contract> findContractByClientId(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findOneContractById(int id);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findAllProjectsWithoutDraftMode();

	@Query("select c from Client c where c.id = :id")
	Client findClientById(int id);

	@Query("Select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("Select c.code from Contract c")
	Collection<String> findAllContractsCode();

	@Query("Select p from ProgressLog p where p.contract.id = :id")
	Collection<ProgressLog> findManyProgressLogByContractId(int id);
}
