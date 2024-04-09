
package acme.features.clients.contracts;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.contract.Contract;
import acme.roles.Client;

@Controller
public class ClientContractController extends AbstractController<Client, Contract> {

	@Autowired
	private ClientContractListService		listService;

	@Autowired
	private ClientContractShowService		showService;

	@Autowired
	private ClientContractUpdateService		updateService;

	@Autowired
	private ClientContractPublishService	publishService;

	@Autowired
	private ClientContractCreateService		createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addBasicCommand("create", this.createService);
	}

}
