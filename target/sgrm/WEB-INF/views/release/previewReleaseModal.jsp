<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="previewReleaseModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="font-20 ">
					<span>Resumen: </span> <span class="font-bold col-cyan">${release.releaseNumber}</span>
				</h5>
			</div>
			<div class="modal-body">
				<section>
					<iframe src="<c:url value='/release/tinySummary-${release.id}'/>"
						id="tinySummary">
						<p>Revisar configuracion del navegador</p>
					</iframe>
				</section>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closePreviewRelease()">CANCELAR</button>
			</div>
		</div>
	</div>
</div>