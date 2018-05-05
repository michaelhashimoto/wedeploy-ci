package io.wedeploy.ci.job;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class BuildTimeTest {

	@Test
	public void testBuildTime() {
		BuildTime buildStartTime = new BuildTime("2018-01-01_00-00-00");

		Assert.assertEquals("2018-01-01_00-00-00", buildStartTime.toString());

		BuildTime buildEndTime = new BuildTime("2018-12-31_23-59-59");

		Assert.assertEquals("2018-12-31_23-59-59", buildEndTime.toString());
	}

}