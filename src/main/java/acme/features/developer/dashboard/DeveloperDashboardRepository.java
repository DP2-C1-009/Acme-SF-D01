
package acme.features.developer.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;

@Repository
public interface DeveloperDashboardRepository extends AbstractRepository {

	@Query("select count(tm) from TrainingModule tm where tm.updateMoment is not null and tm.developer.userAccount.id = :id")
	int totalTrainingModulesWithUpdateMoment(int id);

	@Query("select count(ts) from TrainingSession ts where ts.optionalLink is not null and ts.optionalLink != '' and ts.trainingModule.developer.userAccount.id = :id")
	int totalTrainingSessionsWithLink(int id);

	@Query("select avg(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.userAccount.id = :id")
	Double averageTrainingModulesTime(int id);

	@Query("select sqrt((sum(tm.estimatedTotalTime * tm.estimatedTotalTime) / count(tm.estimatedTotalTime)) - (avg(tm.estimatedTotalTime) * avg(tm.estimatedTotalTime))) from TrainingModule tm where tm.developer.userAccount.id = :id")
	Double deviatonTrainingModulesTime(int id);

	@Query("select min(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.userAccount.id = :id")
	Double minimumTrainingModulesTime(int id);

	@Query("select max(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.userAccount.id = :id")
	Double maximumTrainingModulesTime(int id);

	@Query("select d from Developer d where d.userAccount.id = :id")
	Developer findOneDeveloperById(int id);

	@Query("select tm from TrainingModule tm where tm.developer.userAccount.id = :id")
	Collection<TrainingModule> findAllTrainingModulesByDeveloperId(int id);

	@Query("select ts from TrainingSession ts where ts.trainingModule.developer.userAccount.id = :id")
	Collection<TrainingSession> findAllTrainingSessionsByDeveloperId(int id);

}
