package io.wedeploy.ci.job;

public interface Job {

	public String getHostName();

	public String getLocalURL();

	public String getJobName();

	public String getRemoteURL();

}