<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="io.wedeploy.ci.jenkins.node.JenkinsMaster" %>
<%@ page import="io.wedeploy.ci.jenkins.node.JenkinsMasters" %>
<%@ page import="io.wedeploy.ci.jenkins.node.JenkinsSlave" %>

<html lang="en">
	<head>
		<meta charset="utf-8">

		<spring:url value="main.css" var="mainCSS" />

		<link href="https://cdn.lfrs.sl/alloyui.com/3.0.1/aui-css/css/bootstrap.min.css" rel="stylesheet" />
		<link href="${mainCSS}" rel="stylesheet" />

		<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
	</head>
	<body>
		<h1>Masters</h1>
		<h2>Offline Slave Count: <span id="offline_slave_count">${jenkinsMasters.getOfflineSlaveCount()}</span></h2>

		<div id="masters">
			<c:forEach items="${jenkinsMasters.getJenkinsMasters()}" var="jenkinsMaster">
				<c:if test="${jenkinsMaster.getOfflineSlaveCount() > 0}">
					<h2><a href="${jenkinsMaster.getRemoteURL()}">${jenkinsMaster.getHostname()}</a></h2>

					<ul>
						<c:forEach items="${jenkinsMaster.getJenkinsSlaves()}" var="jenkinsSlave">
							<c:if test="${jenkinsSlave.isOffline()}">
								<li>
									<span><a href="${jenkinsSlave.getRemoteURL()}">${jenkinsSlave.getHostname()}</a></span>
									<span>
										<br />
										<pre>${jenkinsSlave.getOfflineCause()}</pre>
									</span>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</c:if>
			</c:forEach>
		</div>
	</body>
</html>