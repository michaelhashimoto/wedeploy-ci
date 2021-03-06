package io.wedeploy.ci.job;

public interface Build {

	public Integer getBuildNumber();

	public BuildTime getBuildStartTime();

	public String getLocalURL();

	public String getRemoteURL();

	public Result getResult();

	public BuildDuration getTopLevelDuration();

	public BuildDuration getTotalDuration();

	public enum Result {
		ABORTED, FAILURE, IN_PROGRESS, NOT_FOUND, SUCCESS, UNSTABLE
	}

}