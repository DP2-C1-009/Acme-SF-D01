<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.totalNumberCodeAuditsTypeStatic"/>
        </th>
        <td>
            <acme:print value="${totalStaticAudit}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.totalNumberCodeAuditsTypeDynamic"/>
        </th>
        <td>
            <acme:print value="${totalDynamicAudit}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.averageNumberRecords"/>
        </th>
        <td>
            <acme:print value="${averageAuditRecord}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.deviationNumberRecords"/>
        </th>
        <td>
            <acme:print value="${deviationAuditRecord}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.minimumNumberRecords"/>
        </th>
        <td>
            <acme:print value="${minimumAuditRecord}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.maximumNumberRecords"/>
        </th>
        <td>
            <acme:print value="${maximumAuditRecord}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.averagePeriodLength"/>
        </th>
        <td>
            <acme:print value="${averagePeriodAuditRecord}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.deviationPeriodLength"/>
        </th>
        <td>
            <acme:print value="${deviationPeriodAuditRecord}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.minimumPeriodLength"/>
        </th>
        <td>
            <acme:print value="${minimumPeriodAuditRecord}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.maximumPeriodLength"/>
        </th>
        <td>
            <acme:print value="${maximumPeriodAuditRecord}"/>
        </td>
    </tr>
</table>

<acme:return/>