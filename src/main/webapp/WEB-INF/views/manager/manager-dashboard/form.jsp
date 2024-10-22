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
	<acme:message code="manager.manager-dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.total-could-user-stories"/>
		</th>
		<td>
			<acme:print value="${totalCouldUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.total-should-user-stories"/>
		</th>
		<td>
			<acme:print value="${totalShouldUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.total-must-user-stories"/>
		</th>
		<td>
			<acme:print value="${totalMustUserStories}"/>
		</td>
	</tr>		
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.total-wont-user-stories"/>
		</th>
		<td>
			<acme:print value="${totalWontUserStories}"/>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.user-story-estimated-cost-average"/>
		</th>
		<td>
			<jstl:choose>
            <jstl:when test="${empty userStoryEstimatedCostAverage}">
                --
            </jstl:when>
            <jstl:otherwise>
                <acme:print value="${userStoryEstimatedCostAverage}"/>
            </jstl:otherwise>
            </jstl:choose>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.user-story-estimated-cost-deviation"/>
		</th>
		<td>
		<jstl:choose>
            <jstl:when test="${empty userStoryEstimatedCostDeviation}">
                --
            </jstl:when>
            <jstl:otherwise>
			<acme:print value="${userStoryEstimatedCostDeviation}"/>
			</jstl:otherwise>
			</jstl:choose>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.maximum-user-story-estimated-cost"/>
		</th>
		<td>
		<jstl:choose>
            <jstl:when test="${empty maximumUserStoryEstimatedCost}">
                --
            </jstl:when>
            <jstl:otherwise>
			<acme:print value="${maximumUserStoryEstimatedCost}"/>
			</jstl:otherwise>
			</jstl:choose>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.minimum-user-story-estimated-cost"/>
		</th>
		<td>
		<jstl:choose>
            <jstl:when test="${empty minimumUserStoryEstimatedCost}">
                --
            </jstl:when>
            <jstl:otherwise>
			<acme:print value="${minimumUserStoryEstimatedCost}"/>
			</jstl:otherwise>
			</jstl:choose>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.project-cost-average"/>
		</th>
		<td>
		<jstl:choose>
            <jstl:when test="${empty projectCostAverage}">
                --
            </jstl:when>
            <jstl:otherwise>
			<acme:print value="${projectCostAverage}"/>
			</jstl:otherwise>
			</jstl:choose>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.project-cost-deviation"/>
		</th>
		<td>
		<jstl:choose>
            <jstl:when test="${empty projectCostDeviation}">
                --
            </jstl:when>
            <jstl:otherwise>
			<acme:print value="${projectCostDeviation}"/>
			</jstl:otherwise>
			</jstl:choose>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.maximum-project-cost"/>
		</th>
		<td>
		<jstl:choose>
            <jstl:when test="${empty maximumProjectCost}">
                --
            </jstl:when>
            <jstl:otherwise>
		
			<acme:print value="${maximumProjectCost}"/>
			</jstl:otherwise>
			</jstl:choose>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.minimum-project-cost"/>
		</th>
		<td>
		<jstl:choose>
            <jstl:when test="${empty minimumProjectCost}">
                --
            </jstl:when>
            <jstl:otherwise>
			<acme:print value="${minimumProjectCost}"/>
			</jstl:otherwise>
			</jstl:choose>
		</td>
	</tr>	
	
</table>

<div>
	<canvas id="canvas"></canvas>
</div>

<acme:return/>