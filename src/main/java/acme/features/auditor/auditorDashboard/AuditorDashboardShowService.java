
package acme.features.auditor.auditorDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.AuditorDashboard;
import acme.roles.Auditor;

@Service
public class AuditorDashboardShowService extends AbstractService<Auditor, AuditorDashboard> {

	@Autowired
	private AuditorDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		AuditorDashboard dashboard;
		int totalStaticAudit;
		int totalDynamicAudit;

		Double averageAuditRecord;
		Double deviationAuditRecord;

		Integer minimumAuditRecord;
		Integer maximumAuditRecord;

		Double averagePeriodAuditRecord;
		Double deviationPeriodAuditRecord;

		Double minimumPeriodAuditRecord;
		Double maximumPeriodAuditRecord;

		int id;
		id = super.getRequest().getPrincipal().getActiveRoleId();

		totalStaticAudit = this.repository.totalStaticAudit(id);
		totalDynamicAudit = this.repository.totalDynamicAudit(id);

		averageAuditRecord = this.repository.averageAuditRecord(id);
		//deviationAuditRecord = this.repository.deviationAuditRecord(id);

		minimumAuditRecord = this.repository.minimumAuditRecord(id);
		maximumAuditRecord = this.repository.maximumAuditRecord(id);

		averagePeriodAuditRecord = this.repository.averagePeriodAuditRecord(id);
		deviationPeriodAuditRecord = this.repository.deviationPeriodAuditRecord(id);

		minimumPeriodAuditRecord = this.repository.minumPeriodAuditRecord(id);
		maximumPeriodAuditRecord = this.repository.maximumPeriodAuditRecord(id);

		dashboard = new AuditorDashboard();
		dashboard.setAverageAuditRecord(averageAuditRecord);
		dashboard.setAveragePeriodAuditRecord(averagePeriodAuditRecord);
		//dashboard.setDeviationAuditRecord(deviationAuditRecord);
		dashboard.setDeviationPeriodAuditRecord(deviationPeriodAuditRecord);
		dashboard.setMaximumAuditRecord(maximumAuditRecord);
		dashboard.setMaximumPeriodAuditRecord(maximumPeriodAuditRecord);
		dashboard.setMinimumAuditRecord(minimumAuditRecord);
		dashboard.setMinimumPeriodAuditRecord(minimumPeriodAuditRecord);
		dashboard.setTotalDynamicAudit(totalDynamicAudit);
		dashboard.setTotalStaticAudit(totalStaticAudit);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AuditorDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "totalStaticAudit", "totalDynamicAudit", "averageAuditRecord", "deviationAuditRecord", "minimumAuditRecord", "maximumAuditRecord", "averagePeriodAuditRecord", "deviationPeriodAuditRecord", "minimumPeriodAuditRecord",
			"maximumPeriodAuditRecord");

		super.getResponse().addData(dataset);
	}
}
