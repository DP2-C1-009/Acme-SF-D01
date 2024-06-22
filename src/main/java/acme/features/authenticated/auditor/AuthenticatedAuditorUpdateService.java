
package acme.features.authenticated.auditor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuthenticatedAuditorUpdateService extends AbstractService<Authenticated, Auditor> {

	@Autowired
	protected AuthenticatedAuditorRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Auditor auditor;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		auditor = this.repository.findOneAuditorByUserAccountId(userAccountId);

		super.getBuffer().addData(auditor);
	}

	@Override
	public void bind(final Auditor object) {
		assert object != null;

		super.bind(object, "firm", "professionalID", "certifications", "moreInfoLink");
	}

	@Override
	public void validate(final Auditor object) {
		Principal principal;
		int userAccountId;
		Auditor auditor;

		boolean isCodeChanged = false;

		Collection<String> codes = this.repository.findAllProfessionalID();
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		auditor = this.repository.findOneAuditorByUserAccountId(userAccountId);

		if (!super.getBuffer().getErrors().hasErrors("identification")) {
			isCodeChanged = !auditor.getProfessionalID().equals(object.getProfessionalID());
			super.state(!isCodeChanged || !codes.contains(object.getProfessionalID()), "professionalID", "auditor.error.codeDuplicate");
		}
	}

	@Override
	public void perform(final Auditor object) {
		this.repository.save(object);
	}

	@Override
	public void unbind(final Auditor object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "firm", "professionalID", "certifications", "moreInfoLink");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {

		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
