
package acme.features.any.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;

@Repository
public interface AnyContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.draftmode = false")
	Collection<Contract> findContractDraftModeFalse();

	@Query("select c from Contract c where c.id = :id")
	Contract findOneContractById(int id);

}
