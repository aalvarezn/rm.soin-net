<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
			<c:forEach items="${userInfo.authorities}" var="authority">
				<c:if test="${authority.name == 'Admin'}">
					<li><a id="adminItem" href="<c:url value='/admin/'/> "> <span>Administración</span>
					</a></li>
				</c:if>
			</c:forEach>
			<c:set var="releaseAccess" value="false"></c:set>
			<c:forEach items="${userInfo.authorities}" var="authority">
				<c:if test="${authority.name == 'Release Manager'}">
					<li><a id="managemetReleaseItem"
						href="<c:url value='/management/release/'/> "> <span>Gestión
								Release</span>
					</a></li>
					<li><a id="managerRFCItem"
					href="<c:url value='/management/rfc/'/> "> <span>Gestión
							RFC</span>
				</a></li>
					<li><a id="managemetWorkFlowItem"
						href="<c:url value='/management/wf/'/> "> <span>Gestión
								Trámites</span>
					</a></li>
					<li><a id="managemetWorkFlowItem"
						href="<c:url value='/management/wf/'/> "> <span>Gestión
								Solicitudes</span>
					</a></li>
				</c:if>
				<c:if
					test="${authority.name == 'Gestores' or authority.name == 'Desarrolladores'}">
					<c:set var="releaseAccess" value="true"></c:set>
				</c:if>
				<c:if test="${authority.name == 'Gestores'}">
					<c:set var="managerAccess" value="true"></c:set>
				</c:if>
				<c:if test="${authority.name == 'QA'}">
					<c:set var="qaAccess" value="true"></c:set>
				</c:if>
					<c:if test="${authority.name == 'Gestor Solicitudes'}">
					<c:set var="managerRequestAccess" value="true"></c:set>
				</c:if>
			</c:forEach>
			<c:if test="${qaAccess}">
				<li><a id="releasesQAItem" href="<c:url value='/release/qa'/> ">
						<span>Gestión QA</span>
				</a></li>
			</c:if>
				<c:if test="${managerRequestAccess}">
				<li><a id="releasesQAItem" href="<c:url value='/release/qa'/> ">
						<span>Gestión QA</span>
				</a></li>
			</c:if>
			<c:if test="${releaseAccess}">
				<li><a id="releasesItem" href="<c:url value='/release/'/> ">
						<span>Mis Releases</span>
				</a></li>
				<!---- 	#Seccion de reportes ---->
				<li class=""><a id="ambientItem" href="javascript:void(0);"
					class="menu-toggle"> <span>Reportes</span>
				</a>
					<ul class="ml-menu">
						<li><a href="<c:url value='/report/releases/'/>">Releases</a></li>
					</ul></li>
			</c:if>
			<c:if test="${managerAccess}">
				<li><li><a id="RFCItem" href="<c:url value='/rfc/'/> "> <span>
							RFC</span>
				</a></li>
					
				<li><a id="managerWorkFlowItem"
					href="<c:url value='/manager/wf/'/> "> <span>Mis
							Trámites</span>
				</a></li>
			</c:if>
			<li><a id="profileItem" href="<c:url value='/profile/'/> ">
					<span>Perfil de usuario</span>
			</a></li>

		</ul>
	</div>
	<!-- #Menu -->

</aside>