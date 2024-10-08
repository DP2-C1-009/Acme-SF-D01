<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogLessThan25"/>
        </th>
        <td class="align-right">
            <acme:print value="${totalLogLessThan25}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogLessBetween25And50"/>
        </th>
        <td class="align-right">
            <acme:print value="${totalLogLessBetween25And50}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogLessBetween50And75"/>
        </th>
        <td class="align-right">
            <acme:print value="${totalLogLessBetween50And75}"/>
        </td>
    </tr>    
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogAbove75"/>
        </th>
        <td class="align-right">
            <acme:print value="${totalLogAbove75}"/>
        </td>
    </tr>
</table>

<jstl:forEach var="ce" items="${currency}">
    <h3>
        <acme:message code="client.client-dashboard.form.title.Contract"/>
        <acme:message code="${ce}"/>
    </h3>
    
    <table class="table table-sm">
        <tr>
            <th scope="row">
                <acme:message code="client.client-dashboard.form.label.averageBudgetOfContracts"/>
            </th>
            <td class="align-right">
                <acme:print value="${averageBudgetOfContracts[ce] == null? nullValues: averageBudgetOfContracts[ce]}"/>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <acme:message code="client.client-dashboard.form.label.deviationBudgetOfContracts"/>
            </th>
            <td class="align-right">
                <acme:print value="${deviationBudgetOfContracts[ce] == null? nullValues: deviationBudgetOfContracts[ce]}"/>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <acme:message code="client.client-dashboard.form.label.minimunBudgetOfContracts"/>
            </th>
            <td class="align-right">
                <acme:print value="${minimunBudgetOfContracts[ce] == null? nullValues: minimunBudgetOfContracts[ce]}"/>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <acme:message code="client.client-dashboard.form.label.maximumBudgetOfContracts"/>
            </th>
            <td class="align-right">
                <acme:print value="${maximumBudgetOfContracts[ce] == null? nullValues: maximumBudgetOfContracts[ce]}"/>
            </td>
        </tr>
    </table>
</jstl:forEach>
<style>
    .align-right {
        text-align: right;
    }
</style>

<acme:return/>
