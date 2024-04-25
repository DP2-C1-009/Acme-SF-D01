
package acme.features.authenticated.client;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.datatypes.ClientType;
import acme.roles.Client;

@Service
public class AuthenticatedClientCreateService extends AbstractService<Authenticated, Client> {

	@Autowired
	private AuthenticatedClientRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Client.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Client object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Client();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final Client object) {
		assert object != null;

		super.bind(object, "identification", "companyName", "type", "email", "furtherInformation");

	}

	@Override
	public void validate(final Client object) {
		Collection<String> codes = this.repository.findAllCodes();

		if (!super.getBuffer().getErrors().hasErrors("identification"))
			super.state(!codes.contains(object.getIdentification()), "identification", "client.error.codeDuplicate");

	}

	@Override
	public void perform(final Client object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Client object) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(ClientType.class, object.getType());

		dataset = super.unbind(object, "identification", "companyName", "type", "email", "furtherInformation");
		dataset.put("types", choices);
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
