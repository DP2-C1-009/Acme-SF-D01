
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("select s from Sponsorship s where s.sponsor.id = :id")
	Collection<Sponsorship> findManySponsorshipsBySponsorId(int id);

	@Query("select tm.code from TrainingModule tm where tm.developer.id = :id")
	Collection<String> findManyTrainingModuleCodesByDeveloperId(int id);

	@Query("select tm.code from TrainingModule tm")
	Collection<String> findManyTrainingModuleCodes();

	@Query("select tm from TrainingModule tm where tm.id = :id")
	TrainingModule findOneTrainingModuleById(int id);

	@Query("select d from Developer d where d.id = :id")
	Developer findOneDeveloperById(int id);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findManyPublishedProjects();

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :id")
	Collection<TrainingSession> findManyTrainingSessionsByTrainingModuleId(int id);

}
