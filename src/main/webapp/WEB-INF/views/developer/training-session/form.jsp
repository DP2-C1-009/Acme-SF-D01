<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="developer.training-session.form.label.code" path="code"/>
	<acme:input-moment code="developer.training-session.form.label.startDateTime" path="startDateTime"/>
	<acme:input-moment code="developer.training-session.form.label.endDateTime" path="endDateTime"/>
    <acme:input-textbox code="developer.training-session.form.label.location" path="location"/>
    <acme:input-textbox code="developer.training-session.form.label.instructor" path="instructor"/>
	<acme:input-email code="developer.training-session.form.label.contactEmail" path="contactEmail"/>
	<acme:input-url code="developer.training-session.form.label.optionalLink" path="optionalLink"/>
    <acme:input-textbox code="developer.training-session.form.label.draftMode" path="draftMode" readonly="true"/>
    
   	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftmode == true}">
			<acme:input-textbox code="developer.training-session.form.label.trainingModuleCode" path="trainingModuleCode"  readonly="true"/>
<%-- 			<acme:submit code="developer.training-session.form.button.update" action="/developer/training-session/update"/> --%>
<%-- 			<acme:submit code="developer.training-session.form.button.delete" action="/developer/training-session/delete"/> --%>
<%-- 			<acme:submit code="developer.training-session.form.button.publish" action="/developer/training-session/publish"/> --%>
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show') && draftmode == false}">
			<acme:input-textbox code="developer.training-session.form.label.trainingModuleCode" path="trainingModuleCode"/>
		</jstl:when>
		
		<%-- <jstl:when test="${_command == 'create'}">
			<acme:submit code="developer.training-session.list.button.create" action="/developer/training-session/create?trainingModuleId=${contractId}"/>
		</jstl:when> --%>
	</jstl:choose>	
    
</acme:form>