<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="systemContent" name="systemContent"
	value="<c:url value='/'/>" />
<input type="hidden" id="userInfo_username" name="userInfo_username"
	value="${userInfo.userName}" />
<input type="hidden" id="userInfo_Id" name="userInfo_Id"
	value="${userInfo.id}" />
<nav class="navbar">
	<div class="container-fluid">
		<div class="navbar-header">
			<a href="javascript:void(0);" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar-collapse"
				aria-expanded="false"></a> <a href="javascript:void(0);"
				class="bars"></a>
		</div>
		<div class="collapse navbar-collapse" id="navbar-collapse">
			<ul class="nav navbar-nav navbar-right">

				<li><a href="<c:url value='/profile/'/> "><i class="material-icons">person_outline</i>
						<p style="float: right; padding-top: 3px; padding-left: 5px">${userInfo.name}</p></a></li>

				<!-- Notifications -->
				<li class="dropdown"><a href="javascript:void(0);"
					class="dropdown-toggle" data-toggle="dropdown" role="button"> <i
						class="material-icons">notifications</i> <span class="label-count">0</span>
				</a>
					<ul class="dropdown-menu">
						<li class="header">NOTIFICACIONES</li>
						<li class="body">
							<ul class="menu">
								<li><a href="javascript:void(0);">
										<div class="icon-circle bg-light-green">
											<i class="material-icons">mail</i>
										</div>
										<div class="menu-info">
											<h4>Tiene 0 nuevas notificaciones</h4>
											<p>
												<i class="material-icons">access_time</i>hace 0 minuto(s)
											</p>
										</div>
								</a></li>
								<!-- 								<li><a href="javascript:void(0);"> -->
								<!-- 										<div class="icon-circle bg-cyan"> -->
								<!-- 											<i class="material-icons">mail</i> -->
								<!-- 										</div> -->
								<!-- 										<div class="menu-info"> -->
								<!-- 											<h4>Tiene 4 nuevas notificaciones</h4> -->
								<!-- 											<p> -->
								<!-- 												<i class="material-icons">access_time</i>hace 22 minutos -->
								<!-- 											</p> -->
								<!-- 										</div> -->
								<!-- 								</a></li> -->
								<!-- 								<li><a href="javascript:void(0);"> -->
								<!-- 										<div class="icon-circle bg-pink"> -->
								<!-- 											<i class="material-icons">mail</i> -->
								<!-- 										</div> -->
								<!-- 										<div class="menu-info"> -->
								<!-- 											<h4>Tiene 2 nuevas notificaciones</h4> -->
								<!-- 											<p> -->
								<!-- 												<i class="material-icons">access_time</i>hace 3 horas -->
								<!-- 											</p> -->
								<!-- 										</div> -->
								<!-- 								</a></li> -->
								<!-- 								<li><a href="javascript:void(0);"> -->
								<!-- 										<div class="icon-circle bg-orange"> -->
								<!-- 											<i class="material-icons">mail</i> -->
								<!-- 										</div> -->
								<!-- 										<div class="menu-info"> -->
								<!-- 											<h4>Tiene 32 nuevas notificaciones</h4> -->
								<!-- 											<p> -->
								<!-- 												<i class="material-icons">access_time</i>hace 2 horas -->
								<!-- 											</p> -->
								<!-- 										</div> -->
								<!-- 								</a></li> -->
								<!-- 								<li><a href="javascript:void(0);"> -->
								<!-- 										<div class="icon-circle bg-pink"> -->
								<!-- 											<i class="material-icons">mail</i> -->
								<!-- 										</div> -->
								<!-- 										<div class="menu-info"> -->
								<!-- 											<h4>Tiene 64 nuevas notificaciones</h4> -->
								<!-- 											<p> -->
								<!-- 												<i class="material-icons">access_time</i>hace 4 horas -->
								<!-- 											</p> -->
								<!-- 										</div> -->
								<!-- 								</a></li> -->
							</ul>
						</li>
						<li class="footer"><a href="javascript:void(0);">Lista de
								notificaciones</a></li>
					</ul></li>
				<!-- #END# Notifications -->

				<li class="pull-right"><a href="<c:url value='/logout'/> "><i
						class="material-icons">exit_to_app</i></a></li>
			</ul>
		</div>
	</div>
</nav>