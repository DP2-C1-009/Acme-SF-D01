
package acme.features.manager.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select count(u) from UserStory u where u.priority = acme.entities.projects.UserStoryPriority.COULD and u.manager.id = :managerId and u.draftMode = false")
	int totalCouldUserStories(int managerId);

	@Query("select count(u) from UserStory u where u.priority = acme.entities.projects.UserStoryPriority.SHOULD and u.manager.id = :managerId and u.draftMode = false")
	int totalShouldUserStories(int managerId);

	@Query("select count(u) from UserStory u where u.priority = acme.entities.projects.UserStoryPriority.MUST and u.manager.id = :managerId and u.draftMode = false")
	int totalMustUserStories(int managerId);

	@Query("select count(u) from UserStory u where u.priority = acme.entities.projects.UserStoryPriority.WONT and u.manager.id = :managerId and u.draftMode = false")
	int totalWontUserStories(int managerId);

	@Query("select avg(u.estimatedCost) from UserStory u where u.manager.id = :managerId and u.draftMode = false")
	Double userStoryEstimatedCostAverage(int managerId);

	@Query("select max(u.estimatedCost) from UserStory u where u.manager.id = :managerId and u.draftMode = false")
	Integer maximumUserStoryEstimatedCost(int managerId);

	@Query("select min(u.estimatedCost) from UserStory u where u.manager.id = :managerId and u.draftMode = false")
	Integer minimumUserStoryEstimatedCost(int managerId);

	@Query("select avg(p.cost) from Project p where p.manager.id = :managerId and p.draftMode = false")
	Double projectCostAverage(int managerId);

	@Query("select max(p.cost) from Project p where p.manager.id = :managerId and p.draftMode = false")
	Integer maximumProjectCost(int managerId);

	@Query("select min(p.cost) from Project p where p.manager.id = :managerId and p.draftMode = false")
	Integer minimumProjectCost(int managerId);

	@Query("select stddev(u.estimatedCost) from UserStory u where u.manager.id = :managerId and u.draftMode = false")
	Double userStoryEstimatedCostDeviation(int managerId);

	@Query("select stddev(p.cost) from Project p where p.manager.id = :managerId and p.draftMode = false")
	Double projectCostDeviation(int managerId);
}
