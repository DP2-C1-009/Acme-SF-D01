<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="sponsor.invoice.form.label.code" path="code"/>
	
	<jstl:if test="${acme:anyOf(_command, 'show')}">
		<acme:input-moment code="sponsor.invoice.form.label.registrationTime" path="registrationTime" readonly="true"/>
	</jstl:if>
	<acme:input-moment code="sponsor.invoice.form.label.dueDate" path="dueDate"/>
    <acme:input-money code="sponsor.invoice.form.label.quantity" path="quantity"/>
    <acme:input-double code="sponsor.invoice.form.label.tax" path="tax"/>
    <jstl:if test="${acme:anyOf(_command, 'show')}">
    	<acme:input-money code="sponsor.invoice.form.label.totalAmount" path="totalAmount" readonly="true"/>
	</jstl:if>
	<acme:input-url code="sponsor.invoice.form.label.furtherInfo" path="furtherInfo"/>
    
    
    <jstl:if test="${acme:anyOf(_command, 'show')}">
		<acme:input-textbox code="sponsor.invoice.form.label.sponsorship" path="sponsorshipCode" readonly="true"/>
		<acme:input-checkbox code="sponsor.invoice.form.label.draftMode" path="draftMode" readonly="true"/>
	</jstl:if>	
    
   	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:submit code="sponsor.invoice.form.button.update" action="/sponsor/invoice/update"/>
			<acme:submit code="sponsor.invoice.form.button.delete" action="/sponsor/invoice/delete"/>
			<acme:submit code="sponsor.invoice.form.button.publish" action="/sponsor/invoice/publish"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="sponsor.invoice.list.button.create" action="/sponsor/invoice/create?sponsorshipId=${sponsorshipId}"/>
		</jstl:when>
	</jstl:choose>	
    
</acme:form>