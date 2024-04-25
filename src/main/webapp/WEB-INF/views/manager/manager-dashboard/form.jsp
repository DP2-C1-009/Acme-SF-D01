<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<h2>
	<acme:message code="manager.manager-dashboard.form.title.user-stories-by-priority"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.must-user-stories"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfMustUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.should-user-stories"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfShouldUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.could-user-stories"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfCouldUserStories}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.wont-user-stories"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfWontUserStories}"/>
		</td>
	</tr>
</table>


<h2>
	<acme:message code="manager.manager-dashboard.form.title.operations-user-stories"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.average-cost-user-stories"/>
		</th>
		<td>
			<acme:print value="${averageEstimatedCostUserStories == null ? 'No data' : averageEstimatedCostUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.deviation-cost-user-stories"/>
		</th>
		<td>
			<acme:print value="${deviationEstimatedCostUserStories == null ? 'No data' : deviationEstimatedCostUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.minimum-cost-user-stories"/>
		</th>
		<td>
			<acme:print value="${minEstimatedCostUserStories == null ? 'No data' : minEstimatedCostUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.maximum-cost-user-stories"/>
		</th>
		<td>
			<acme:print value="${maxEstimatedCostUserStories == null ? 'No data' : maxEstimatedCostUserStories}"/>
		</td>
	</tr>
</table>


<h2>
	<acme:message code="manager.manager-dashboard.form.title.operations-projects"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.average-cost-projects"/>
		</th>
		<td>
			<acme:print value="${averageCostProjects == null ? 'No data' : averageCostProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.deviation-cost-projects"/>
		</th>
		<td>
			<acme:print value="${deviationCostProjects == null ? 'No data' : deviationCostProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.minimum-cost-projects"/>
		</th>
		<td>
			<acme:print value="${minCostProjects == null ? 'No data' : minCostProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.maximum-cost-projects"/>
		</th>
		<td>
			<acme:print value="${maxCostProjects == null ? 'No data' : maxCostProjects}"/>
		</td>
	</tr>
</table>


<acme:return/>