<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="manager.made-of.form.label.project" path="work" choices="${projects}" readonly="${acme:anyOf(_command, 'show|delete')}"/>
	<acme:input-select code="manager.made-of.form.label.user-stories" path="story" choices="${userStories}" readonly="${acme:anyOf(_command, 'show|delete')}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:submit code="manager.made-of.form.button.delete" action="/manager/made-of/delete" />
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.made-of.form.button.create" action="/manager/made-of/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>