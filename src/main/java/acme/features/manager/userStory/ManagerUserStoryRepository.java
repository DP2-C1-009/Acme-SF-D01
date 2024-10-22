
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

	@Query("select u from UserStory u where u.id = :id")
	UserStory findOneUserStoryById(int id);

	@Query("select u from UserStory u where u.manager.id = :managerId")
	Collection<UserStory> findAllUserStoriesByManagerId(int managerId);

	@Query("select mo.story from MadeOf mo where mo.work.id = :projectId")
	Collection<UserStory> findAllUserStoriesByProjectId(int projectId);

	@Query("select m from Manager m where m.id = :managerId")
	Manager findManagerById(int managerId);

	@Query("select mo from MadeOf mo where mo.story.id = :userStoryId")
	Collection<MadeOf> findMadeOfsByUserStoryId(int userStoryId);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectByProjectId(int projectId);

}
