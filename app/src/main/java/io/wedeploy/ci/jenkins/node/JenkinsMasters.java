package io.wedeploy.ci.jenkins.node;

import java.util.List;

public interface JenkinsMasters {

	public int getOfflineSlaveCount();

	public List<JenkinsMaster> getJenkinsMasters();

	public String getOverviewInformation();

}