
package acme.features.sponsor.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("select s from Sponsor s where s.userAccount.id = :id")
	Sponsor findOneSponsorById(int id);

	@Query("select s from Sponsorship s where s.sponsor.userAccount.id = :id")
	Collection<Sponsorship> findAllSponsorshipsBySponsorId(int id);

	@Query("select i from Invoice i where i.sponsorship.sponsor.userAccount.id = :id")
	Collection<Invoice> findAllInvoicesBySponsorId(int id);

	@Query("select count(i) from Invoice i where i.tax <= 21 and i.sponsorship.sponsor.userAccount.id = :sponsorId")
	int findInvoicesTaxLessOrEq21(int sponsorId);

	@Query("select count(s) from Sponsorship s where s.furtherInfo is not null and s.sponsor.userAccount.id = :sponsorId")
	int findSponsorshipsWithLink(int sponsorId);

	@Query("SELECT AVG(s.amount.amount) FROM Sponsorship s where s.sponsor.userAccount.id = :sponsorId")
	double findAverageSponsorshipsAmount(int sponsorId);

	@Query("select sqrt((sum(s.amount.amount * s.amount.amount) / count(s.amount.amount)) - (avg(s.amount.amount) * avg(s.amount.amount))) from Sponsorship s where s.sponsor.userAccount.id = :sponsorId")
	double findDeviationSponsorshipsAmount(int sponsorId);

	@Query("select max(s.amount.amount) from Sponsorship s where s.sponsor.userAccount.id = :sponsorId")
	double findMaximumSponsorshipsAmount(int sponsorId);

	@Query("select min(s.amount.amount) from Sponsorship s where s.sponsor.userAccount.id = :sponsorId")
	double findMinimumSponsorshipsAmount(int sponsorId);

	@Query("SELECT AVG(i.quantity.amount) FROM Invoice i where i.sponsorship.sponsor.userAccount.id = :sponsorId")
	double findAverageInvoicesQuantity(int sponsorId);

	@Query("select sqrt((sum(i.quantity.amount * i.quantity.amount) / count(i.quantity.amount)) - (avg(i.quantity.amount) * avg(i.quantity.amount))) from Invoice i where i.sponsorship.sponsor.userAccount.id = :sponsorId")
	double findDeviationInvoicesQuantity(int sponsorId);

	@Query("select max(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.userAccount.id = :sponsorId")
	double findMaximumInvoicesQuantity(int sponsorId);

	@Query("select min(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.userAccount.id = :sponsorId")
	double findMinimumInvoicesQuantity(int sponsorId);

}
