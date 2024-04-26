<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogLessThan25"/>
		</th>
		<td>
			<acme:print value="${totalLogLessThan25}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogLessBetween25And50"/>
		</th>
		<td>
			<acme:print value="${totalLogLessBetween25And50}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogLessBetween50And75"/>
		</th>
		<td>
			<acme:print value="${totalLogLessBetween50And75}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogAbove75"/>
		</th>
		<td>
			<acme:print value="${totalLogAbove75}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.averageBudgetOfContracts"/>
		</th>
		<td>
			<acme:print value="${averageBudgetOfContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.deviationBudgetOfContracts"/>
		</th>
		<td>
			<acme:print value="${deviationBudgetOfContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.minimunBudgetOfContracts"/>
		</th>
		<td>
			<acme:print value="${minimunBudgetOfContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.maximumBudgetOfContracts"/>
		</th>
		<td>
			<acme:print value="${maximumBudgetOfContracts}"/>
		</td>
	</tr>

</table>
<acme:return/>