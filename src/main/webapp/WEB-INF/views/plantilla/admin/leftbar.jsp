<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<aside id="leftsidebar" class="sidebar">
	<!-- User Info -->
	<div class="user-info">
		<div class="image">
			<a href="<c:url value='/admin/'/> "> <img
				src="<c:url value='/static/images/logo_interno.svg'/>" width="150"
				height="40" alt="User" />
			</a>
		</div>

	</div>
	<!-- #User Info -->
	<!-- Menu -->
	<div id="menuListItems" class="menu">
		<ul class="list">
			<li class=""><a href="<c:url value='/'/> "> <span>
						Sitio Web</span>
			</a></li>
			<li class=""><a href="javascript:void(0);" class="menu-toggle">
					<span>Configuración</span>
			</a>
				<ul class="ml-menu">
					<li><a id="systemConfigItem"
						href="<c:url value='/admin/email/'/>">Correos</a></li>
					<li><a href="<c:url value='/admin/parameter/'/>">Parámetros</a></li>
				</ul></li>
			<li class=""><a href="javascript:void(0);" class="menu-toggle">
					<span>Releases</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/impact/'/>">Impacto</a></li>
					<li><a href="<c:url value='/admin/priority/'/>">Prioridad</a></li>
					<li><a href="<c:url value='/admin/risk/'/>">Riesgo</a></li>
				</ul></li>

			<li class=""><a href="javascript:void(0);" class="menu-toggle">
					<span>Sistemas</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/systemConfig/'/>">Secciones</a></li>
				</ul></li>
			<li class=""><a href="javascript:void(0);" class="menu-toggle">
					<span>Usuarios</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/user/'/>">Usuarios</a></li>
				</ul></li>
			<li class=""><a href="<c:url value='/admin/project/'/>"> <span>
						Proyectos</span>
			</a></li>
		</ul>
	</div>
	<!-- #Menu -->

</aside>