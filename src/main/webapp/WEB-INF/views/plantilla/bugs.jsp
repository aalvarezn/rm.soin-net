<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div class="row clearfix">
	<div class="col-sm-12">
		<h5 class="titulares">Seccion bugs</h5>
	</div>


	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label for="to" class="col-sm-4 col-form-label lbtxt m-t-11"
			style="width: 165px;">Bugs: </label>
		<div class="col-sm-6">
			<div class="form-group m-b-0i">
				<div class="form-line">
					<input type="text" id="bugs" name="bugs" value="${bugs}"
						class="form-control tagInitBugs" placeholder="">
				</div>
				<label id="bugs_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>

</div>
