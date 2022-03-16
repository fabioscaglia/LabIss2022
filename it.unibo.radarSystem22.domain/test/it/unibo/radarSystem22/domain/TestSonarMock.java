package it.unibo.radarSystem22.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class TestSonarMock {
	
	@Before
	public void up()
	{
		System.out.println("UP");
	}
	
	@After
	public void down()
	{
		System.out.println("DOWN");
	}

	@Test
	public void testSonarMock() {
		DomainSystemConfig.simulation=true;
		DomainSystemConfig.testing=false;
		DomainSystemConfig.sonarDelay=250;
		
		ISonar sonar = DeviceFactory.createSonar();
		new SonarConsumerForTesting(sonar,DomainSystemConfig.sonarDelay).start();
		sonar.activate();
		while(sonar.isActive())
		{
			BasicUtils.delay(1000);
		}
	}
	
	/*
	@Test
	public void testActive() {
		ISonar sonar = new SonarMock();
		sonar.activate();
		assertTrue(sonar.isActive()== true);
		sonar.deactivate();
		assertTrue(sonar.isActive()== false);
	}
	
	@Test
	public void testDistance() {
		ISonar sonar = new SonarMock();
		sonar.activate();
		long delay = 1000;
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		IDistance d = sonar.getDistance();
		int curDistanceCalcolata=(int) (90-delay/500);
		//System.out.println(curDistanceCalcolata +" "+d.getVal());
		assertTrue(d.getVal()== curDistanceCalcolata);
		sonar.deactivate();
		
	}
	*/
}
