<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<aside id="leftsidebar" class="sidebar">
            <!-- User Info -->
            <div class="user-info">
                <div class="image">
                <a href="<c:url value='/'/> ">
                    <img src="<c:url value='/static/images/logo_interno.svg'/>" width="150" height="40" alt="User" />
                    </a>
                </div>
               
            </div>
            <!-- #User Info -->
            <!-- Menu -->
            <div id="menuListItems" class="menu">
                <ul class="list">
                    <li class="active">
                        <a id="releaseItem" href="<c:url value='/'/> ">
                            <span>Mis Releases</span>
                        </a>
                    </li>					
				</ul>
            </div>
            <!-- #Menu -->
            
        </aside>