
package acme.features.manager.madeOf;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.MadeOf;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerMadeOfRepository extends AbstractRepository {

	@Query("SELECT mo FROM MadeOf mo WHERE mo.id = :id")
	MadeOf findOneMadeOfById(int id);

	@Query("SELECT mo FROM MadeOf mo WHERE mo.work.manager.id = :id AND mo.story.manager.id = :id")
	Collection<MadeOf> findMadeOfsByManagerId(int id);

	@Query("SELECT m FROM Manager m WHERE m.id = :id")
	Manager findOneManagerById(int id);

	@Query("SELECT mo FROM MadeOf mo WHERE mo.work.id = :id")
	Collection<MadeOf> findMadeOfsByProjectId(int id);

	@Query("SELECT mo FROM MadeOf mo WHERE mo.story.id = :id")
	Collection<MadeOf> findMadeOfsByUserStoryId(int id);

	@Query("SELECT mo FROM MadeOf mo WHERE mo.work.id = :projectId AND mo.story.id = :userStoryId")
	MadeOf findOneMadeOfByProjectIdAndUserStoryId(int projectId, int userStoryId);

	@Query("SELECT us FROM UserStory us WHERE us.manager.id = :id")
	Collection<UserStory> findUserStoriesByManagerId(int id);

	@Query("SELECT p FROM Project p WHERE p.manager.id = :id")
	Collection<Project> findProjectsByManagerId(int id);

	@Query("SELECT mo.work FROM MadeOf mo WHERE mo.id = :id")
	Project findOneProjectByMadeOfId(int id);

	@Query("SELECT mo.story FROM MadeOf mo WHERE mo.id = :id")
	UserStory findOneUserStoryByMadeOfId(int id);

	@Query("SELECT mo.work.manager FROM MadeOf mo WHERE mo.id = :id")
	Manager findOneManagerByMadeOfId(int id);

}
