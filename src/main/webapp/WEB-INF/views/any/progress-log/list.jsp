<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.progressLog.list.label.recordId" path="recordId"/>
	<acme:list-column code="any.progressLog.list.label.completeness" path="completeness"/>
	<acme:list-column code="any.progressLog.list.label.responsiblePerson" path="responsiblePerson"/>
</acme:list>