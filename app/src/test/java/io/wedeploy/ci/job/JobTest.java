package io.wedeploy.ci.job;

import org.junit.Test;

public class JobTest {

	@Test
	public void testBaseJob() throws Exception {
		Job job = new BaseJob("http://test-1-1/job/test-portal-acceptance-upstream(master)/");

		System.out.println(job.getName());
	}

}