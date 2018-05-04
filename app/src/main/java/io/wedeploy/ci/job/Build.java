package io.wedeploy.ci.job;

public interface Build {

	public Integer getBuildNumber();

	public String getLocalURL();

	public String getRemoteURL();

}