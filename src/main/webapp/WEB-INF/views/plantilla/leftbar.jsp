<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<aside id="leftsidebar" class="sidebar">
	<!-- User Info -->
	<div class="user-info">
		<div class="image">
			<a href="<c:url value='/'/> "> <img
				src="<c:url value='/static/images/logo_interno.svg'/>" width="150"
				height="40" alt="User" />
			</a>
		</div>

	</div>
	<!-- #User Info -->
	<!-- Menu -->
	<div id="menuListItems" class="menu">
		<ul class="list">
			<security:authorize access="hasRole('DEVELOPER')" var="isDeveloper" />
			<security:authorize access="hasRole('ADMIN')" var="isAdmin" />
			<security:authorize access="hasRole('RM')" var="isReleaseManager" />
			<security:authorize access="hasRole('MANAGER')" var="isManager" />

			<c:if test="${isAdmin}">
				<li><a id="adminItem" href="<c:url value='/admin/'/> "> <span>Administración</span>
				</a></li>
			</c:if>
			<c:if test="${isReleaseManager}">
				<li><a id="managemetReleaseItem"
					href="<c:url value='/management/release/'/> "> <span>RM
							Release</span>
				</a></li>
				<li><a id="managemetWorkFlowItem"
					href="<c:url value='/management/wf/'/> "> <span>RM
							Trámites</span>
				</a></li>

				<!---- 	#Seccion de reportes ---->
				<li class=""><a id="ambientItem" href="javascript:void(0);"
					class="menu-toggle"> <span>Reportes</span>
				</a>
					<ul class="ml-menu">
						<li><a href="<c:url value='/report/releases/'/>">Releases</a></li>
					</ul></li>
			</c:if>

			<c:if test="${ isDeveloper}">
				<li><a id="releasesItem" href="<c:url value='/release/'/> ">
						<span>Mis Releases</span>
				</a></li>
			</c:if>

			<c:if test="${ isManager}">
				<li><a id="managerReleasesItem" href="<c:url value='/release/'/> ">
						<span>Gestión Releases</span>
				</a></li>
				<li><a id="managerRFCItem" href="<c:url value='/rfc/'/> ">
						<span>Gestión RFCs</span>
				</a></li>
			</c:if>
	
			<li><a id="rfcItem" href="<c:url value='/rfc/'/> ">
						<span>RFC</span>
				</a></li>
			<li><a id="profileItem" href="<c:url value='/profile/'/> ">
					<span>Perfil de usuario</span>
			</a></li>

		</ul>
	</div>
	<!-- #Menu -->

</aside>