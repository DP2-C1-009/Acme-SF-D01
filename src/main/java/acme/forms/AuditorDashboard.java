
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuditorDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	int							totalStaticAudit;
	int							totalDynamicAudit;

	Double						averageAuditRecord;
	Double						deviationAuditRecord;

	Integer						minimumAuditRecord;
	Integer						maximumAuditRecord;

	Double						averagePeriodAuditRecord;
	Double						deviationPeriodAuditRecord;

	Double						minimumPeriodAuditRecord;
	Double						maximumPeriodAuditRecord;

}
