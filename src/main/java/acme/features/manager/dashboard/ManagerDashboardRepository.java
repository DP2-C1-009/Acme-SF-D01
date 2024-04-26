
package acme.features.manager.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.UserStoryPriority;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("SELECT COUNT(us) FROM UserStory us WHERE us.priority = :p AND us.manager.id = :id")
	int totalNumberOfPriorityUserStories(UserStoryPriority p, int id);

	@Query("SELECT AVG(us.estimatedCost) FROM UserStory us WHERE us.manager.id = :id")
	Double averageEstimatedCostUserStories(int id);

	@Query("SELECT STDDEV(us.estimatedCost) FROM UserStory us WHERE us.manager.id = :id")
	Double deviationEstimatedCostUserStories(int id);

	@Query("SELECT MIN(us.estimatedCost) FROM UserStory us WHERE us.manager.id = :id")
	Double minEstimatedCostUserStories(int id);

	@Query("SELECT MAX(us.estimatedCost) FROM UserStory us WHERE us.manager.id = :id")
	Double maxEstimatedCostUserStories(int id);

	@Query("SELECT AVG(p.cost) FROM Project p WHERE p.manager.id = :id")
	Double averageCostProjects(int id);

	@Query("SELECT STDDEV(p.cost) FROM Project p WHERE p.manager.id = :id")
	Double deviationCostProjects(int id);

	@Query("SELECT MIN(p.cost) FROM Project p WHERE p.manager.id = :id")
	Double minCostProjects(int id);

	@Query("SELECT MAX(p.cost) FROM Project p WHERE p.manager.id = :id")
	Double maxCostProjects(int id);
}
