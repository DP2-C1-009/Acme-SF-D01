
package acme.components;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.banner.Banner;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query("select count(b) from Banner b where b.start < :currentDate and b.end > :currentDate")
	int countCurrentBanners(Date currentDate);

	@Query("select b from Banner b where b.start < :currentDate and b.end > :currentDate")
	List<Banner> findManyCurrentBanners(PageRequest pageRequest, Date currentDate);

	default Banner findRandomBanner() {
		Banner banner;
		int count;
		int index;
		PageRequest page;

		Date currentDate;
		List<Banner> listBanner;

		currentDate = MomentHelper.getCurrentMoment();
		count = this.countCurrentBanners(currentDate);

		if (count == 0)
			banner = null;
		else {
			index = RandomHelper.nextInt(0, count);

			page = PageRequest.of(index, 1, Sort.by(Direction.ASC, "id"));
			listBanner = this.findManyCurrentBanners(page, currentDate);

			banner = listBanner.isEmpty() ? null : listBanner.get(0);
		}

		return banner;
	}

}
