<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="sponsor.invoice.form.label.code" path="code"/>
	<acme:input-moment code="sponsor.invoice.form.label.registrationTime" path="registrationTime" readonly="true"/>
	<acme:input-moment code="sponsor.invoice.form.label.dueDate" path="dueDate"/>
    <acme:input-money code="sponsor.invoice.form.label.quantity" path="quantity"/>
    <acme:input-double code="sponsor.invoice.form.label.tax" path="tax"/>
    <acme:input-money code="sponsor.invoice.form.label.totalAmount" path="totalAmount" readonly="true"/>
	<acme:input-url code="sponsor.invoice.form.label.furtherInfo" path="furtherInfo"/>
    <acme:input-checkbox code="sponsor.invoice.form.label.draftMode" path="draftMode" readonly="true"/>
    
   	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:input-textbox code="sponsor.invoice.form.label.sponsorship" path="sponsorshipCode" readonly="true"/>
			<acme:submit code="sponsor.invoice.form.button.update" action="/sponsor/invoice/update"/>
			<acme:submit code="sponsor.invoice.form.button.delete" action="/sponsor/invoice/delete"/>
			<acme:submit code="sponsor.invoice.form.button.publish" action="/sponsor/invoice/publish"/>
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
			<acme:input-textbox code="sponsor.invoice.form.label.sponsorship" path="sponsorshipCode" readonly="true"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:input-textbox code="sponsor.invoice.form.label.sponsorship" path="sponsorshipCode" readonly="true"/>
			<acme:submit code="sponsor.invoice.list.button.create" action="/sponsor/invoice/create?sponsorshipId=${sponsorshipId}"/>
		</jstl:when>
	</jstl:choose>	
    
</acme:form>