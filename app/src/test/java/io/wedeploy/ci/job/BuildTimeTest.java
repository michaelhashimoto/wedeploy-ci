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

	@Test
	public void testAddTime() {
		BuildTime buildTime = new BuildTime("2018-01-01_00-00-00");

		BuildTime buildTime1 = new BuildTime(buildTime);

		buildTime1.addYears(2);

		Assert.assertEquals("2020-01-01_00-00-00", buildTime1.toString());

		BuildTime buildTime2 = new BuildTime(buildTime);

		buildTime2.addMonths(2);

		Assert.assertEquals("2018-03-01_00-00-00", buildTime2.toString());

		buildTime2.addDays(15);

		Assert.assertEquals("2018-03-16_00-00-00", buildTime2.toString());

		buildTime2.addDays(15);

		Assert.assertEquals("2018-03-31_00-00-00", buildTime2.toString());

		buildTime2.addDays(1);

		Assert.assertEquals("2018-04-01_00-00-00", buildTime2.toString());
	}

}