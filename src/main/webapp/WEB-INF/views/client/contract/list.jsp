<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.contract.list.label.code" path="code"/>
	<acme:list-column code="client.contract.list.label.providerName" path="providerName"/>
	<acme:list-column code="client.contract.list.label.draftMode" path="draftmode"/>
</acme:list>
<acme:button code="client.contract.list.button.create" action="/client/contract/create"/>