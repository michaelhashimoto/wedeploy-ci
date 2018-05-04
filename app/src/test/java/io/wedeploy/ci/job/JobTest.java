package io.wedeploy.ci.job;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class JobTest {

	@Test
	public void testBuild() {
		List<Build> builds = _job.getCompletedBuilds();

		Build build = builds.get(5);

		System.out.println("BUILD_NUMBER:\t" + build.getBuildNumber());
		System.out.println("LOCAL_URL:\t\t" + build.getLocalURL());
		System.out.println("REMOTE_URL:\t\t" + build.getRemoteURL());
		System.out.println("BUILD_RESULT:\t" + build.getResult());
		System.out.println("BUILD_START_TIME:\t" + build.getBuildStartTime());
		System.out.println("TOP_LEVEL_DURATION:\t" + build.getTopLevelDuration());
		System.out.println("TOTAL_DURATION:\t" + build.getTotalDuration());
	}

	@Test
	public void testGetBuilds() {
		System.out.println("2017 - " + _job.getCompletedBuildsByYear(2017).size());
		System.out.println("2018 - " + _job.getCompletedBuildsByYear(2018).size());
		System.out.println();
		System.out.println("April 2018 - " + _job.getCompletedBuildsByMonth(2018, 4).size());
		System.out.println("May 2018 - " + _job.getCompletedBuildsByMonth(2018, 5).size());
		System.out.println("June 2018 - " + _job.getCompletedBuildsByMonth(2018, 6).size());
		System.out.println();
		System.out.println("April-June 2018 - " + _job.getCompletedBuildsByMonth(2018, 4, 3).size());
		System.out.println("May-June 2018 - " + _job.getCompletedBuildsByMonth(2018, 5, 2).size());
		System.out.println();
		System.out.println("April 30, 2018 - " + _job.getCompletedBuildsByDay(2018, 4, 30).size());
		System.out.println("May 1, 2018 - " + _job.getCompletedBuildsByDay(2018, 5, 1).size());
		System.out.println("May 2, 2018 - " + _job.getCompletedBuildsByDay(2018, 5, 2).size());
		System.out.println("May 3, 2018 - " + _job.getCompletedBuildsByDay(2018, 5, 3).size());
		System.out.println("May 4, 2018 - " + _job.getCompletedBuildsByDay(2018, 5, 4).size());
		System.out.println("May 5, 2018 - " + _job.getCompletedBuildsByDay(2018, 5, 5).size());
		System.out.println();
		System.out.println("May 1-2, 2018 - " + _job.getCompletedBuildsByDay(2018, 5, 1, 2).size());
		System.out.println("May 3-5, 2018 - " + _job.getCompletedBuildsByDay(2018, 5, 3, 3).size());
	}

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

	private static Job _job = new BaseJob("http://test-1-1/job/test-portal-acceptance-pullrequest(master)");

}