
package acme.features.any.progessLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Any;
import acme.entities.progressLogs.ProgressLog;

@Controller
public class AnyPogressLogController extends AbstractController<Any, ProgressLog> {

	@Autowired
	private AnyProgressLogListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
