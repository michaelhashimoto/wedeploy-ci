package io.wedeploy.ci.job;

import java.util.List;

public interface Job {

	public List<Build> getCompletedBuilds();

	public List<Build> getCompletedBuildsByDay(Integer year, Integer month, Integer day);

	public List<Build> getCompletedBuildsByDay(Integer year, Integer month, Integer day, Integer numberOfDays);

	public List<Build> getCompletedBuildsByMonth(Integer year, Integer month);

	public List<Build> getCompletedBuildsByMonth(Integer year, Integer month, Integer numberOfMonths);

	public List<Build> getCompletedBuildsByYear(Integer year);

	public List<Build> getCompletedBuildsByYear(Integer year, Integer yearsToAdd);

	public String getHostName();

	public String getLocalURL();

	public String getJobName();

	public String getRemoteURL();

}