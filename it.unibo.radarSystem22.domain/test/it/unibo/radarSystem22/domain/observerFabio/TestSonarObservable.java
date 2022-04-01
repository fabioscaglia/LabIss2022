package it.unibo.radarSystem22.domain.observerFabio;
import static org.junit.Assert.assertTrue;
import org.junit.*;

import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 

public class TestSonarObservable {
	@Before
	public void up() {
		System.out.println("up");
	}
	
	@After
	public void down() {
		System.out.println("down");		
	}	
	
	@Test 
	public void testSonarMock() {
		DomainSystemConfig.simulation = true;
		DomainSystemConfig.sonarObservable = true;
		//DomainSystemConfig.testingDistance = 50;
		DomainSystemConfig.testing    = false;
		DomainSystemConfig.sonarDelay = 10;		//quite fast generation ...
		int delta = 1;
		
		ISonar sonar = DeviceFactory.createSonar(DomainSystemConfig.sonarObservable);
		new SonarObserverFortesting("observer1", (ISonarObservable)sonar, false);  //consuma
		sonar.activate();		
 		while( sonar.isActive() ) { BasicUtils.delay(1000);} //to avoid premature exit
 	}
	
 
}
