
package acme.features.manager.madeOf;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projects.MadeOf;
import acme.roles.Manager;

@Controller
public class ManagerMadeOfController extends AbstractController<Manager, MadeOf> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerMadeOfListService	listService;

	@Autowired
	private ManagerMadeOfShowService	showService;

	@Autowired
	private ManagerMadeOfCreateService	createService;

	@Autowired
	private ManagerMadeOfDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);

	}

}
