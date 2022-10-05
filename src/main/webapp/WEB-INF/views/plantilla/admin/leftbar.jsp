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
			<!---- 	#Seccion de pagina de inicio ---->
			<li class=""><a href="<c:url value='/'/> "> <span>Página
						principal</span></a></li>
			<!---- 	#Seccion de proyectos ---->
			<li class=""><a id="treeItem"
				href="<c:url value='/admin/tree/'/>"> <span>Árbol dependencias</span>
			</a></li>
			<!---- 	#Seccion de ambientes ---->
			<li class=""><a id="ambientItem" href="javascript:void(0);"
				class="menu-toggle"> <span>Ambientes</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/typeAmbient/'/>">Tipos
							de ambiente</a></li>
					<li><a href="<c:url value='/admin/ambient/'/>">Ambientes</a></li>
				</ul></li>
			<!---- 	#Seccion de configuracion ---->
			<li class=""><a id="configurationItem"
				href="javascript:void(0);" class="menu-toggle"> <span>Configuración</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/email/'/>">Correos</a></li>
					<li><a href="<c:url value='/admin/parameter/'/>">Parámetros</a></li>
				</ul></li>
			<!---- 	#Seccion de documentos ---->
			<li class=""><a id="documentItem" href="javascript:void(0);"
				class="menu-toggle"> <span>Documentos</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/gDoc/'/>">Gdocs Excel</a></li>
					<li><a href="<c:url value='/admin/docTemplate/'/>">Plantilla
							Release</a></li>
					<li><a href="<c:url value='/admin/docFile/'/>">Plantilla
							Archivo</a></li>
				</ul></li>
			<!---- 	#Seccion de releases ---->
			<li class=""><a id="releaseItem" href="javascript:void(0);"
				class="menu-toggle"> <span>Catalogos</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/error/'/>">Errores</a></li>
					<li><a href="<c:url value='/admin/status/'/>">Estados</a></li>
					<li><a href="<c:url value='/admin/statusRFC/'/>">Estados RFC</a></li> 
					<li><a href="<c:url value='/admin/impact/'/>">Impacto</a></li>
					<li><a href="<c:url value='/admin/priority/'/>">Prioridad</a></li>
					<li><a href="<c:url value='/admin/risk/'/>">Riesgo</a></li>
					<li><a href="<c:url value='/admin/typeChange/'/>">Tipo Cambio</a></li> 
				</ul></li>
			<!---- 	#Seccion de requerimientos ---->
			<li class=""><a id="requestItem" href="javascript:void(0);"
				class="menu-toggle"> <span>Requerimientos</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/request/'/>">Requerimientos</a></li>
					<li><a href="<c:url value='/admin/typeRequest/'/>">Tipo
							Requerimiento</a></li>
				</ul></li>
			<!---- 	#Seccion de sistemas ---->
			<li class=""><a id="systemItem" href="javascript:void(0);"
				class="menu-toggle"> <span>Sistemas</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/action/'/>">Acciones</a></li>
					<li><a href="<c:url value='/admin/environment/'/>">Entorno</a></li>
					<li><a href="<c:url value='/admin/siges/'/>">Código Siges</a></li>
					<li><a href="<c:url value='/admin/configurationItem/'/>">Item
							Configuración</a></li>
					<li><a href="<c:url value='/admin/module/'/>">Modulo</a></li>
					<li><a href="<c:url value='/admin/typeObject/'/>">Tipo
							Objeto</a></li>
					<li><a href="<c:url value='/admin/systemConfig/'/>">Secciones</a></li>
					<li><a href="<c:url value='/admin/system/'/>">Sistemas</a></li>
				</ul></li>
			<!---- 	#Seccion de usuarios ---->
			<li class=""><a id="userItem" href="javascript:void(0);"
				class="menu-toggle"> <span>Usuarios</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/admin/user/'/>">Usuarios</a></li>
					<li><a href="<c:url value='/admin/authority/'/>">Roles</a></li>
				</ul></li>
			<!---- 	#Seccion de proyectos ---->
			<li class=""><a id="projectItem"
				href="<c:url value='/admin/project/'/>"> <span> Proyectos</span>
			</a></li>
			<!---- 	#Seccion de tramites ---->
			<li class=""><a id="wfItem" href="javascript:void(0);"
				class="menu-toggle"> <span>Tramites</span>
			</a>
				<ul class="ml-menu">
					<li><a href="<c:url value='/wf/type/'/>">Tipos</a></li>
					<li><a href="<c:url value='/wf/workFlow/'/>">Trámites</a></li>
				</ul></li>
		</ul>
	</div>
	<!-- #Menu -->

</aside>