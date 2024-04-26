
package acme.features.sponsor.invoices;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorInvoiceRepository extends AbstractRepository {

	@Query("select s from Sponsorship s where s.sponsor.id = :id")
	Collection<Sponsorship> findManySponsorshipsBySponsorId(int id);

	@Query("select s.code from Sponsorship s")
	Collection<String> findManySponsorshipCodes();

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("select s from Sponsor s where s.id = :id")
	Sponsor findOneSponsorById(int id);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findManyPublishedProjects();

	@Query("select i from Invoice i where i.sponsorship.id = :id")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int id);

	@Query("select s.draftMode from Sponsorship s where s.id = :id")
	boolean findSponsorshipDraftModeById(int id);

	@Query("select s from Sponsorship s where s.draftMode = true")
	Collection<Sponsorship> findSponsorshipsInDraftMode();

	@Query("select i from Invoice i where i.id = :id")
	Invoice findOneInvoiceById(int id);

	@Query("select i.code from Invoice i")
	Collection<String> findManyInvoiceCodes();

}
