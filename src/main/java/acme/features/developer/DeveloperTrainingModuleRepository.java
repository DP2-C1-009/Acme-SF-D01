
package acme.features.developer;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training.TrainingModule;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select tm from TrainingModule tm where tm.developer.id = :id")
	Collection<TrainingModule> findManyTrainingModulesByDeveloperId(int id);

	@Query("select tm from TrainingModule tm where tm.id = :id")
	TrainingModule findOneTrainingModuleById(int id);

}
