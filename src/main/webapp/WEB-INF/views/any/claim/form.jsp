<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
<jstl:choose>
	<jstl:when test="${acme:anyOf(_command, 'show')}">
		<acme:input-textbox code="any.claim.form.label.code" path="code" readonly = "true"/>	
		<acme:input-textbox code="any.claim.form.label.heading" path="heading" readonly = "true"/>
		<acme:input-textbox code="any.claim.form.label.description" path="description" readonly = "true"/>
		<acme:input-textbox code="any.claim.form.label.department" path="department" readonly = "true"/>
		<acme:input-textbox code="any.claim.form.label.email" path="email" readonly = "true"/>
		<acme:input-moment code="any.claim.form.label.instantiationMoment" path="instatiationMoment" readonly = "true"/>
		
		<acme:input-url code="any.claim.form.label.optionalLink" path="optionalLink" readonly = "true"/>
	</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'create')}">
		<acme:input-textbox code="any.claim.form.label.code" path="code" />	
		<acme:input-textbox code="any.claim.form.label.heading" path="heading"/>
		<acme:input-textbox code="any.claim.form.label.description" path="description" />
		<acme:input-textbox code="any.claim.form.label.department" path="department" />
		<acme:input-textbox code="any.claim.form.label.email" path="email" />
		<acme:input-url code="any.claim.form.label.optionalLink" path="optionalLink"/>
		<acme:input-checkbox code="any.claim.form.label.confirmation" path="confirmation"/>
		<acme:submit code="any.claim.form.button.create" action="/any/claim/create"/>
	</jstl:when>
</jstl:choose>
</acme:form>