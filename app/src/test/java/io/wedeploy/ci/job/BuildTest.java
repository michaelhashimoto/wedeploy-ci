package io.wedeploy.ci.job;

import org.junit.Assert;
import org.junit.Test;

public class BuildTest {

	@Test
	public void testBuild() throws Exception {
		Job job = new BaseJob("http://test-1-1/job/test-portal-acceptance-upstream(master)");

		for (int i = 1; i < 10; i++) {
			String buildURL = job.getLocalURL() + "/" + i;

			Build build = new BaseBuild(job, buildURL);

			Assert.assertEquals(new Integer(i), build.getBuildNumber());
			Assert.assertEquals("http://test-1-1/job/test-portal-acceptance-upstream(master)/" + i, build.getLocalURL());
			Assert.assertEquals("https://test-1-1.lax.liferay.com/job/test-portal-acceptance-upstream(master)/" + i, build.getRemoteURL());
		}
	}

}