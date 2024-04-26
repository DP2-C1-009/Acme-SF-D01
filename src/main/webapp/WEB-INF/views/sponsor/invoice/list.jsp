<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="sponsor.invoice.list.label.code" path="code"/>
	<acme:list-column code="sponsor.invoice.list.label.dueDate" path="dueDate"/>
	<acme:list-column code="sponsor.invoice.list.label.totalAmount" path="totalAmount"/>
</acme:list>

<jstl:if test="${sponsorshipDraftMode == true}">
	<acme:button code="sponsor.invoice.list.button.create" action="/sponsor/invoice/create?sponsorshipId=${sponsorshipId}"/>
</jstl:if>