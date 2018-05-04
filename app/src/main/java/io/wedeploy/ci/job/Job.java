package io.wedeploy.ci.job;

import java.util.List;

public interface Job {

	public List<Build> getCompletedBuilds();

	public String getHostName();

	public String getLocalURL();

	public String getJobName();

	public String getRemoteURL();

}