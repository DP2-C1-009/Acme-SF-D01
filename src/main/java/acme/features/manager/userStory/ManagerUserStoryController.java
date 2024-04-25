
package acme.features.manager.userStory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Controller
public class ManagerUserStoryController extends AbstractController<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerUserStoryListService		listService;

	@Autowired
	protected ManagerUserStoryShowService		showService;

	@Autowired
	protected ManagerUserStoryCreateService		createService;

	@Autowired
	protected ManagerUserStoryUpdateService		updateService;

	@Autowired
	protected ManagerUserStoryDeleteService		deleteService;

	@Autowired
	protected ManagerUserStoryPublishService	publishService;

	@Autowired
	protected ManagerUserStoryListAllService	listAllService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);

		super.addCustomCommand("list-by-projects", "list", this.listService);
		super.addCustomCommand("list-mine", "list", this.listAllService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
