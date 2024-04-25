
package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.MadeOf;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerUserStoryRepository extends AbstractRepository {

	@Query("select mo from MadeOf mo where mo.work.id = :id")
	Collection<MadeOf> findMadeOfByProjectId(int id);

	@Query("select mo from MadeOf mo where mo.story.id = :id")
	Collection<MadeOf> findMadeOfByUserStoryId(int id);

	@Query("select us from UserStory us where us.id = :id")
	UserStory findUserStoryById(int id);

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select m from Manager m where m.id = :id")
	Manager findManagerById(int id);

	@Query("select us from UserStory us where us.manager.id = :id")
	Collection<UserStory> findUserStoriesByManagerId(int id);

	@Query("select mo from MadeOf mo where mo.work.id = :projectId and mo.story.id = :userStoryId")
	Collection<MadeOf> findMadeOfByProjectIdAndUserStoryId(int projectId, int userStoryId);

}
