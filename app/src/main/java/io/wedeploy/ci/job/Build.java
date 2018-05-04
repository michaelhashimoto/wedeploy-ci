package io.wedeploy.ci.job;

public interface Build {

	public Integer getBuildNumber();

	public String getLocalURL();

	public String getRemoteURL();

	public Result getResult();

	public enum Result {
		ABORTED, FAILURE, NOT_BUILT, NOT_FOUND, SUCCESS, UNSTABLE
	}

}