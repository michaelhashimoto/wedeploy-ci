package io.wedeploy.ci.job;

public interface Build {

	public Integer getBuildNumber();

	public BuildStartTime getBuildStartTime();

	public String getLocalURL();

	public String getRemoteURL();

	public Result getResult();

	public BuildDuration getTopLevelDuration();

	public BuildDuration getTotalDuration();

	public enum Result {
		ABORTED, FAILURE, NOT_BUILT, NOT_FOUND, SUCCESS, UNSTABLE
	}

}