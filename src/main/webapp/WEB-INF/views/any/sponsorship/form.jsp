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

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.sponsorship.form.label.code" path="code"/>
	
	<jstl:if test="${acme:anyOf(_command, 'show|update|publish|delete')}">
		<acme:input-moment code="any.sponsorship.form.label.moment" path="moment" readonly="true"/>
	</jstl:if>
	
	<acme:input-moment code="any.sponsorship.form.label.start" path="start"/>
	<acme:input-moment code="any.sponsorship.form.label.end" path="end"/>
	<acme:input-money code="any.sponsorship.form.label.amount" path="amount"/>
	<acme:input-email code="any.sponsorship.form.label.email" path="email"/>
	<acme:input-textbox code="any.sponsorship.form.label.further-info" path="furtherInfo"/>
	<acme:input-select code="any.sponsorship.form.label.type" path="type" choices="${types}"/>	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
			<acme:input-select code="any.sponsorship.form.label.projects" path="project" choices="${projects}" readonly="true"/>
			
		</jstl:when>
	</jstl:choose>	
</acme:form>

