<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.auditrecord.list.label.code" path="code"/>
	<acme:list-column code="auditor.auditrecord.list.label.mark" path="mark"/>
</acme:list>
<acme:button code="auditor.auditrecord.form.button.create" action="/auditor/audit-record/create?codeAuditId=${codeAuditId}"/>	
