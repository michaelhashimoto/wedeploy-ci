package io.wedeploy.ci.job;

import org.junit.Assert;
import org.junit.Test;

public class JobTest {

	@Test
	public void testBaseJob() throws Exception {
		Job job = new BaseJob("http://test-1-1/job/test-portal-acceptance-upstream(master)");

		Assert.assertEquals("test-portal-acceptance-upstream(master)", job.getJobName());
		Assert.assertEquals("http://test-1-1/job/test-portal-acceptance-upstream(master)", job.getLocalURL());
		Assert.assertEquals("https://test-1-1.lax.liferay.com/job/test-portal-acceptance-upstream(master)", job.getRemoteURL());

		job = new BaseJob("https://test-1-1.lax.liferay.com/job/test-portal-acceptance-upstream(master)");

		Assert.assertEquals("test-portal-acceptance-upstream(master)", job.getJobName());
		Assert.assertEquals("http://test-1-1/job/test-portal-acceptance-upstream(master)", job.getLocalURL());
		Assert.assertEquals("https://test-1-1.lax.liferay.com/job/test-portal-acceptance-upstream(master)", job.getRemoteURL());
	}

}