<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.contract.form.label.code" path="code" placeholder="client.contract.form.recordId.placeholder"/>
	<acme:input-textbox code="client.contract.form.label.providerName" path="providerName"/>
	<acme:input-textbox code="client.contract.form.label.customerName" path="customerName"/>
	<acme:input-moment code="client.contract.form.label.instantiationMoment" path="instantiationMoment" readonly="true"/>
    <acme:input-textarea code="client.contract.form.label.goals" path="goals"/>
    <acme:input-money code="client.contract.form.label.budget" path="budget"/>
    <acme:input-select code="client.contract.form.label.projects" path="project" choices="${projects}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftmode == true}">
		    <acme:submit code="client.contract.form.button.update" action="/client/contract/update"/>
		    <acme:submit code="client.contract.form.button.publish" action="/client/contract/publish"/>
		     <acme:submit code="client.contract.form.button.delete" action="/client/contract/delete"/>
		</jstl:when>	
		
		<jstl:when test="${acme:anyOf(_command, 'show') && draftmode == false}">
			<acme:button code="client.progressLog.form.button.progressLog" action="/client/progress-log/list?contractId=${id}"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">	
			<acme:submit code="client.contract.form.button.create" action="/client/contract/create"/>
		</jstl:when>	
	</jstl:choose>	
</acme:form>