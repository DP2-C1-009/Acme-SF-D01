
package acme.features.manager.dashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.entities.projects.UserStoryPriority;
import acme.entities.systemConfiguration.SystemConfiguration;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select us from UserStory us where us.manager.id = :managerId")
	Collection<UserStory> findUserStoriesByManagerId(int managerId);

	@Query("select p from Project p where p.manager.id = :managerId")
	Collection<Project> findProjectsByManagerId(int managerId);

	@Query("select COUNT(us) FROM UserStory us WHERE us.priority = :p AND us.manager.id = :id AND us.draftMode = :draftMode")
	int totalNumberOfPriorityUserStories(UserStoryPriority p, int id, boolean draftMode);

	@Query("SELECT AVG(us.estimatedCost) FROM UserStory us WHERE us.manager.id = :id AND us.draftMode = :draftMode")
	Double averageEstimatedCostUserStories(int id, boolean draftMode);

	@Query("SELECT STDDEV(us.estimatedCost) FROM UserStory us WHERE us.manager.id = :id AND us.draftMode = :draftMode")
	Double deviationEstimatedCostUserStories(int id, boolean draftMode);

	@Query("SELECT MIN(us.estimatedCost) FROM UserStory us WHERE us.manager.id = :id AND us.draftMode = :draftMode")
	Double minEstimatedCostUserStories(int id, boolean draftMode);

	@Query("SELECT MAX(us.estimatedCost) FROM UserStory us WHERE us.manager.id = :id AND us.draftMode = :draftMode")
	Double maxEstimatedCostUserStories(int id, boolean draftMode);

	@Query("select sc from SystemConfiguration sc")
	List<SystemConfiguration> findSystemConfiguration();
}
