
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
	private ManagerUserStoryListMineService	listMineService; //userStories de un proyecto

	@Autowired
	private ManagerUserStoryListService		listService; //userStories de un manager

	@Autowired
	private ManagerUserStoryPublishService	publishService;

	@Autowired
	private ManagerUserStoryShowService		showService;

	@Autowired
	private ManagerUserStoryCreateService	createService;

	@Autowired
	private ManagerUserStoryUpdateService	updateService;

	@Autowired
	private ManagerUserStoryDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("list", this.listService);

		super.addCustomCommand("list-mine", "list", this.listMineService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
