<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.claim.list.label.code" path="code"/>
	<acme:list-column code="any.claim.list.label.instantiationMoment" path="instatiationMoment"/>
	<acme:list-column code="any.claim.list.label.heading" path="heading"/>
	<acme:list-column code="any.claim.list.label.description" path="description"/>		
</acme:list>

<acme:button code="any.claim.list.button.create" action="/any/claim/create"/>