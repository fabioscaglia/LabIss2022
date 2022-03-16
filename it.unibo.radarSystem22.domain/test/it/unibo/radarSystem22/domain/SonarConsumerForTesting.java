package it.unibo.radarSystem22.domain;

import static org.junit.Assert.assertTrue;

import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.*;


public class SonarConsumerForTesting extends Thread {
	
	private ISonar sonar;
	private int delta;
	public SonarConsumerForTesting(ISonar sonar, int delta) {
		super();
		this.sonar = sonar;
		this.delta = delta;
	}
	
	@Override
	public void run()
	{
		int d0 = sonar.getDistance().getVal(); //valore della distanza iniziale
		ColorsOut.out("SonarConsumerForTesting | initial value d0=" + d0);
		
		while(d0==90)
		{
			//il sonar non è ancora stato fatto partire
			BasicUtils.delay(DomainSystemConfig.sonarDelay);
			d0 = sonar.getDistance().getVal();
			ColorsOut.out("SonarConsumerForTesting | initial value =" + d0);
		}
		//esco da questo while solo quando il sonar inizia a produrre valori diversi da 90, quindi quando 
		//è attivo
		while(sonar.isActive())
		{
			BasicUtils.delay(DomainSystemConfig.sonarDelay/2);
			int d = sonar.getDistance().getVal();
			ColorsOut.out("SonarConsumerForTesting | value d = "+d);
			int dExpectedMin = d0-delta;
			int dExpectedMax = d0+delta;
			assertTrue(  d <= dExpectedMax && d >= dExpectedMin );
			d0 = d;
		}
	}

}
