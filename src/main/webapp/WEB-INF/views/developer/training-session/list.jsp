<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="developer.training-session.list.label.code" path="code"/>
	<acme:list-column code="developer.training-session.list.label.startDateTime" path="startDateTime"/>
	<acme:list-column code="developer.training-session.list.label.instructor" path="instructor"/>
</acme:list>