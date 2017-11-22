<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="io.wedeploy.ci.jenkins.JenkinsLegion" %>
<%@ page import="io.wedeploy.ci.jenkins.JenkinsCohort" %>
<%@ page import="io.wedeploy.ci.jenkins.JenkinsMaster" %>
<%@ page import="io.wedeploy.ci.jenkins.JenkinsSlave" %>

<html lang="en">
	<head>
		<meta charset="utf-8">

		<spring:url value="main.css" var="mainCSS" />

		<link href="https://cdn.lfrs.sl/alloyui.com/3.0.1/aui-css/css/bootstrap.min.css" rel="stylesheet" />
		<link href="${mainCSS}" rel="stylesheet" />

		<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
	</head>
	<body>
		<h1>Jenkins Cohorts</h1>
		<p>Last updated ${jenkinsLegion.getLastUpdate()}</p>

		<c:forEach items="${jenkinsLegion.getCohorts()}" var="jenkinsCohort">
			<h2>${jenkinsCohort.getName()}</h2>
			<h4>Cohort Offline Slave Count: ${jenkinsCohort.getOfflineSlaveCount()}</h4>

			<c:forEach items="${jenkinsCohort.getMasters()}" var="jenkinsMaster">
				<c:if test="${jenkinsMaster.getOfflineSlaveCount() > 0}">
					<h4><a href="${jenkinsMaster.getRemoteURL()}">${jenkinsMaster.getName()}</a></h4>

					<details>
						<summary>Master Offline Slave Count: ${jenkinsMaster.getOfflineSlaveCount()}</summary>
						<ul>
							<c:forEach items="${jenkinsMaster.getSlaves()}" var="jenkinsSlave">
								<c:if test="${jenkinsSlave.isOffline()}">
									<li>
										<a href="${jenkinsSlave.getRemoteURL()}">${jenkinsSlave.getName()}</a>

										<br />

										<span><pre>${jenkinsSlave.getOfflineCause()}</pre></span>
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</details>
				</c:if>
			</c:forEach>
		</c:forEach>
	</body>
</html>