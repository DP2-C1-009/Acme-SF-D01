
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

	@Query("select m from Manager m where m.id = :managerId")
	Manager findOneManagerById(int managerId);

	@Query("select p from Project p where p.manager.id = :managerId and p.draftMode = :draftMode")
	Collection<Project> findUnpublishedProjectsByManagerId(int managerId, boolean draftMode);

	@Query("select p from Project p where p.manager.id = :managerId")
	Collection<Project> findProjectsByManagerId(int managerId);

	@Query("select u from UserStory u where u.manager.id = :managerId and u.draftMode = :draftMode")
	Collection<UserStory> findPublishedUserStoriesByManagerId(int managerId, boolean draftMode);

	@Query("select u from UserStory u where u.manager.id = :managerId")
	Collection<UserStory> findUserStoriesByManagerId(int managerId);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select u from UserStory u where u.id = :userStoryId")
	UserStory findOneUserStoryById(int userStoryId);

	@Query("select mo from MadeOf mo where mo.id = :madeOfId")
	MadeOf findOneMadeOfById(int madeOfId);

	@Query("select mo from MadeOf mo where mo.id = :madeOfId")
	Collection<MadeOf> findMadeOfsById(int madeOfId);

	@Query("select mo from MadeOf mo where mo.work.manager.id = :managerId and mo.story.manager.id = :managerId")
	Collection<MadeOf> findMadeOfsByManagerId(int managerId);

}
