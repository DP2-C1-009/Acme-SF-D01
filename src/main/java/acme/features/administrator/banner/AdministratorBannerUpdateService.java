
package acme.features.administrator.banner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

@Service
public class AdministratorBannerUpdateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBannerRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int id;
		Banner banner;

		id = super.getRequest().getData("id", int.class);
		banner = this.repository.findBannerById(id);
		status = banner != null && super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Banner object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findBannerById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;

		super.bind(object, "moment", "start", "end", "picture", "slogan", "web");
	}

	@Override
	public void validate(final Banner object) {
		assert object != null;
		Date instantiation = object.getMoment();
		Date start = object.getStart();
		Date finish = object.getEnd();

		if (!super.getBuffer().getErrors().hasErrors("start"))
			super.state(instantiation != null && start.after(instantiation), //
				"start", "administrator.banner.form.error.must-start-after-instantiation");

		if (!super.getBuffer().getErrors().hasErrors("end")) {
			super.state(start != null && finish.after(start), "end", "administrator.banner.form.error.must-end-after-start");

			if (start != null && finish.after(start)) {
				long diffInMillies = Math.abs(finish.getTime() - start.getTime());
				long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
				super.state(diff >= 7, "end", "administrator.banner.form.error.display-period-too-short");
			}
		}
	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "moment", "start", "end", "picture", "slogan", "web");

		super.getResponse().addData(dataset);

	}

}
