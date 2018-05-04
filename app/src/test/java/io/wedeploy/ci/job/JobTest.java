package io.wedeploy.ci.job;

import org.junit.Assert;
import org.junit.Test;

public class JobTest {

	@Test
	public void testJob() throws Exception {
		String[] jobURLs = new String[] {
			"http://test-1-1/job/test-portal-acceptance-upstream(master)",
			"http://test-1-1/job/test-portal-acceptance-upstream(master)/",
			"https://test-1-1.liferay.com/job/test-portal-acceptance-upstream(master)",
			"https://test-1-1.liferay.com/job/test-portal-acceptance-upstream(master)/"
		};

		for (String jobURL : jobURLs) {
			Job job = new BaseJob(jobURL);

			Assert.assertEquals("test-portal-acceptance-upstream(master)", job.getJobName());
			Assert.assertEquals("http://test-1-1/job/test-portal-acceptance-upstream(master)", job.getLocalURL());
			Assert.assertEquals("https://test-1-1.liferay.com/job/test-portal-acceptance-upstream(master)", job.getRemoteURL());
		}
	}

	@Test
	public void testReadCurrentBuilds() {
		BaseJob baseJob = new BaseJob("http://test-1-1/job/test-portal-acceptance-pullrequest(master)");

		baseJob.readCurrentBuilds();
	}

}