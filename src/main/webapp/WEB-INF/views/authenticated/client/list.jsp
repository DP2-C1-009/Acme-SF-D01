<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.client.list.label.identification" path="identification"/>
	<acme:list-column  code="authenticated.client.list.label.companyName" path="companyName"/>
	<acme:list-column code="authenticated.client.list.label.type" path="type"/>
	<acme:list-column code="authenticated.client.list.label.email" path="email"/>
	<acme:list-column  code="authenticated.client.list.label.furtherInformation" path="furtherInformation"/>
</acme:list>