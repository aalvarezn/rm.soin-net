<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="systemContent" name="systemContent" value="<c:url value='/'/>" />
<input type="hidden" id="userInfo_username" name="userInfo_username" value="${userInfo.username}" />
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

				<li><a ><i class="material-icons">person_outline</i>
					<p style="float: right; padding-top: 3px; padding-left: 5px">${name }</p></a></li>
				<li class="pull-right">
					<a href="<c:url value='/logout'/> "><i class="material-icons">exit_to_app</i></a>
				</li>
			</ul>
		</div>
	</div>
</nav>