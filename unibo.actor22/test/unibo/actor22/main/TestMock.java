package unibo.actor22.main;

import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.common.RadarSystemConfig;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.utils.CommSystemConfig;

public class TestMock {

	public static void main(String[] args) {
		DomainSystemConfig.tracing      = false;	
		DomainSystemConfig.simulation	= true;
		DomainSystemConfig.testing		=false;
		DomainSystemConfig.ledGui		= true;
 		CommSystemConfig.protcolType    = ProtocolType.tcp;
		CommSystemConfig.tracing        = false;
		RadarSystemConfig.testing		=false;
		
		ISonar sonar = DeviceFactory.createSonar();
		sonar.activate();
		for(int i=0; i<30; i++)
		{
			System.out.println(sonar.getDistance());
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sonar.deactivate();

	}

}
