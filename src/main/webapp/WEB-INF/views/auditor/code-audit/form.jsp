<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	
	<acme:input-textbox code="auditor.codeaudit.list.label.code" path="code"/>
	<acme:input-moment code="auditor.codeaudit.list.label.execution" path="execution" readonly = "true" />
	<acme:input-select code="auditor.codeaudit.list.label.type" path="type" choices="${types}"/>
	<acme:input-textbox code="auditor.codeaudit.list.label.correctiveActions" path="correctiveActions"/>
	<acme:input-url code="auditor.codeaudit.list.label.moreInfoLink" path="moreInfoLink"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.codeaudit.form.button.create" action="/auditor/code-audit/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="auditor.codeaudit.form.button.update" action="/auditor/code-audit/update"/>
			<acme:submit code="auditor.codeaudit.form.button.delete" action="/auditor/code-audit/delete"/>
		</jstl:when>
	</jstl:choose>
</acme:form>