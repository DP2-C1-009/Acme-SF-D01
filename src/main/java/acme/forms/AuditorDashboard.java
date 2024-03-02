
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuditorDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	protected int				totalStaticAudit;
	protected int				totalDynamicAudit;

	protected double			averageAuditRecord;
	protected double			deviationAuditRecord;

	protected int				minimumAuditRecord;
	protected int				maximumAuditRecord;

	protected double			averagePeriodAuditRecord;
	protected double			deviationPeriodAuditRecord;

	protected int				minimumPeriodAuditRecord;
	protected int				maximumPeriodAuditRecord;

}
