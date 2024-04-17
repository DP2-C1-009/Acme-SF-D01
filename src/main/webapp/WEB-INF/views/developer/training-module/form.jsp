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
	<acme:input-textbox code="developer.training-module.form.label.code" path="code"/>
	<acme:input-textarea code="developer.training-module.form.label.details" path="details"/>
	<acme:input-select code="developer.training-module.form.label.difficulty-level" path="difficultyLevel" choices="${difficultyLevels}"/>
	<acme:input-moment code="developer.training-module.form.label.update-moment" path="updateMoment"/>
	<acme:input-url code="developer.training-module.form.label.optional-link" path="optionalLink"/>
	<acme:input-integer code="developer.training-module.form.label.estimated-total-time" path="estimatedTotalTime"/>
	<acme:input-textbox code="developer.training-module.form.label.draft-mode" path="draftMode" readonly="true"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftmode == true}">
			<acme:input-moment code="developer.training-module.form.label.creation-moment" path="creationMoment" readonly="true"/>
			<acme:input-textbox code="developer.training-module.form.label.projectCode" path="projectCode" readonly="true"/>
			
		    <%-- <acme:submit code="developer.training-module.form.button.update" action="/developer/training-module/update"/>
		    <acme:submit code="developer.training-module.form.button.publish" action="/developer/training-module/publish"/>
		    <acme:submit code="developer.training-module.form.button.delete" action="/developer/training-module/delete"/> --%>
		</jstl:when>	
		
		<jstl:when test="${acme:anyOf(_command, 'show') && draftmode == false}">
			<acme:input-moment code="developer.training-module.form.label.creation-moment" path="creationMoment"/>
			<acme:input-textbox code="developer.training-module.form.label.projectCode" path="projectCode"/>
			
<%-- 			<acme:button code="developer.training-session.form.button.training-session" action="/developer/training-session/list?training-module=${id}"/> --%>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:input-moment code="developer.training-module.form.label.creation-moment" path="creationMoment" readonly="true"/>
			<acme:input-select code="developer.training-module.form.label.projects" path="project" choices="${projects}"/>
			
			<acme:submit code="developer.training-module.form.button.create" action="/developer/training-module/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>

